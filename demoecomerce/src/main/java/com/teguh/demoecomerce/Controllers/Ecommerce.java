package com.teguh.demoecomerce.Controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teguh.demoecomerce.Models.Checkout;
import com.teguh.demoecomerce.Models.Items;
import com.teguh.demoecomerce.Service.ChartService;
import com.teguh.demoecomerce.Service.ItemsService;
import com.teguh.demoecomerce.Utils.CheckUtil;
import com.teguh.demoecomerce.Utils.Constants;
import com.teguh.demoecomerce.Utils.GeneratorJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Ecommerce {

    @Autowired
    private ItemsService itemsService;
    private Items items;

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "v1/search/{param}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getoneItem(
            @RequestHeader(required = false, value = "pembeli") String pembeli,
            @RequestHeader(required =  false, value= "token") String token,
            @PathVariable("param") String param
    ){
        Map<String, Object> response = new HashMap<>();
        items = new Items();
        items = itemsService.getBynama(param);

        try{
            //ini cek token
            if (CheckUtil.isNullOrEmpty(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.USERNAME_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } else if (CheckUtil.isNullOrEmpty(token)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Claims claims = GeneratorJWT.validateToken(token);

            if (!claims.getId().equals(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_NOT_MATCH + pembeli);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (ExpiredJwtException expired){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_EXPIRED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(SignatureException signature){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(Exception e){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            //cek token sampai sini
        }

        if(items != null){
            response.put(Constants.DATA_KEY, items);
            response.put(Constants.STATUS,Constants.SUCCESS);
            response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            response.put(Constants.STATUS,Constants.ITEM_NOT_FOUND);
            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


    }


    @RequestMapping(value = "v1/additem", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getid(
            @RequestHeader(required =  false, value= "token") String token,
            @RequestParam(required = false) Map<String, String> params,
            @RequestHeader(required = false, value = "pembeli") String pembeli
    ){
        Map<String, Object> response = new HashMap<>();
        int oke = 0;
        try{
            //ini cek token
            if (CheckUtil.isNullOrEmpty(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.USERNAME_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } else if (CheckUtil.isNullOrEmpty(token)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Claims claims = GeneratorJWT.validateToken(token);

            if (!claims.getId().equals(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_NOT_MATCH + pembeli);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (ExpiredJwtException expired){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_EXPIRED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(SignatureException signature){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(Exception e){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            //cek token sampai sini
        }
        oke = 0;

        oke = chartService.insertone(params.get("items"),params.get("jumlah"),pembeli);

        if(oke == 2) {
            response.put(Constants.STATUS, Constants.ITEM_NOT_FOUND);
            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(oke == 3){
            response.put(Constants.STATUS, Constants.CUSTOMER_NOT_FOUND);
            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(oke == 0){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, "Duplicate items : " + params.get("items"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        response.put(Constants.STATUS, Constants.SUCCES_ADD_TO_CHART);
        response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "v1/addmore", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addbatch(
            @RequestBody  final Map<String, Object>Request,
            @RequestHeader(required =  false, value= "token") String token,
            @RequestHeader(required = false, value = "pembeli") String pembeli
    ) throws JsonProcessingException {
        int oke = 0;
        Map<String, Object> response = new HashMap<>();
        try{
            //ini cek token
            if (CheckUtil.isNullOrEmpty(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.USERNAME_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } else if (CheckUtil.isNullOrEmpty(token)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Claims claims = GeneratorJWT.validateToken(token);

            if (!claims.getId().equals(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_NOT_MATCH + pembeli);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (ExpiredJwtException expired){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_EXPIRED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(SignatureException signature){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(Exception e){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            //cek token sampai sini
        }
        String jsonString = new ObjectMapper().writeValueAsString(Request.get(Constants.DATA_KEY));
        List<String> ret = new ArrayList<>();
        ret = chartService.insertbatch(jsonString,pembeli);

            if(ret.size()>0) {
                String s = ret.get(0);

                        if (s == "notfound") {
                            response.put(Constants.STATUS, Constants.CUSTOMER_NOT_FOUND);
                            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

                        } else {
                            String failed = String.join(",", ret );
                            response.put(Constants.STATUS, failed);
                            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                        }
                } else{

                    response.put(Constants.STATUS, Constants.ALL_ITEM_SSUCCES_ADD_TOCHART);
                    response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

    }


    @RequestMapping(value = "v1/delete/{param}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deletecghart(
            @PathVariable("param") String param,
            @RequestHeader(required =  false, value= "token") String token,
            @RequestHeader(required = false, value = "pembeli") String pembeli
    ) {
        int oke = 0;

        Map<String, Object> response = new HashMap<>();
        try{
            //ini cek token
            if (CheckUtil.isNullOrEmpty(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.USERNAME_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } else if (CheckUtil.isNullOrEmpty(token)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Claims claims = GeneratorJWT.validateToken(token);

            if (!claims.getId().equals(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_NOT_MATCH + pembeli);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (ExpiredJwtException expired){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_EXPIRED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(SignatureException signature){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(Exception e){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            //cek token sampai sini
        }
        oke = chartService.DeleteCart(param, pembeli);

            if (oke == 1) {
                response.put(Constants.STATUS, Constants.SUCCESS);
                response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
                return new ResponseEntity<>(response, HttpStatus.OK);

            }else if (oke == 3) {
                response.put(Constants.STATUS, Constants.CUSTOMER_NOT_FOUND);
                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            }else if (oke == 2) {
                response.put(Constants.STATUS, Constants.ITEM_NOT_FOUND);
                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else{
                response.put(Constants.STATUS, Constants.ITEM_NOT_FOUND);
                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
    }



    @RequestMapping(value = "v1/chart", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> listChekout(
            @RequestHeader(required =  false, value= "token") String token,
            @RequestHeader(required = false, value = "pembeli") String pembeli
    ){

        int total = 0;
        Map<String, Object> response = new HashMap<>();
        try{
            //ini cek token
            if (CheckUtil.isNullOrEmpty(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.USERNAME_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } else if (CheckUtil.isNullOrEmpty(token)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_EMPTY);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Claims claims = GeneratorJWT.validateToken(token);

            if (!claims.getId().equals(pembeli)) {

                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                response.put(Constants.STATUS, Constants.TOKEN_NOT_MATCH + pembeli);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }catch (ExpiredJwtException expired){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_EXPIRED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(SignatureException signature){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }catch(Exception e){

            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            response.put(Constants.STATUS, Constants.TOKEN_FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            //cek token sampai sini
        }
       List <Checkout>  listcheckout= new ArrayList<>();
        listcheckout = chartService.listchart(pembeli);
        if(listcheckout != null) {
            if (listcheckout.size() > 0) {
                for(Checkout m : listcheckout){
                    total = total + m.getTotalharga();
                }
                response.put(Constants.DATA_KEY, listcheckout);
                response.put(Constants.TOTAL_PAY, total);
                response.put(Constants.STATUS, Constants.SUCCESS);
                response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put(Constants.STATUS, Constants.ITEM_NOT_FOUND);
                response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }else{
            response.put(Constants.STATUS, Constants.CUSTOMER_NOT_FOUND);
            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}

