package com.teguh.demoecomerce.Controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teguh.demoecomerce.Models.Pembeli;
import com.teguh.demoecomerce.Service.PembeliService;
import com.teguh.demoecomerce.Utils.Constants;
import com.teguh.demoecomerce.Utils.GeneratorJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GenerateTokenbylogin {
    private Gson gson = new GsonBuilder().create();

    @Autowired
    PembeliService pembeliService;

    @RequestMapping(value = "v1/gettoken", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> Login(
            @RequestBody final Map<String, Object>Request
    ) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<String, Object>();

            Map<String, Object> loginMap = (Map<String, Object>) Request.get(Constants.DATA_KEY);
           Pembeli mpembeli = new Pembeli();
            String jsonString = new ObjectMapper().writeValueAsString(loginMap);
            mpembeli = gson.fromJson(jsonString, Pembeli.class);

            if (mpembeli != null) {
                String in_username = "", in_password = "", dataPassword = "", token = "";
                in_username = mpembeli.getNama().toString();
                in_password = mpembeli.getPassword().toString();

                Pembeli cek = new Pembeli();
                cek = pembeliService.search(in_username);


                if (cek == null) {
                    response.put(Constants.STATUS, Constants.USERNAME_NOT_REGISTERED);
                    response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);

                } else{


                    if (cek.getNama().equals(in_username)) // Kalau username terdaftar pada DB
                    {

                        dataPassword = cek.getPassword(); // Ambil data password dari DB berdasarkan username

                        if (in_password.equals(dataPassword)) {

                            //codingan generate token
                            token = GeneratorJWT.createToken(in_username);
                            response.put(Constants.TOKEN, token);

                            response.put(Constants.STATUS, Constants.SUCCESS);
                            response.put(Constants.STATUS_CODE, Constants.SUCCESS_CODE);
                        } else {

                            response.put(Constants.STATUS, Constants.PASSWORD_WRONG);
                            response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);

                        }
                    } else {
                        response.put(Constants.STATUS, Constants.USERNAME_WRONG);
                        response.put(Constants.STATUS_CODE, Constants.FAILED_CODE);
                    }
                }
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put(Constants.STATUS, Constants.Failed);
                response.put(Constants.STATUS_CODE,Constants.FAILED_CODE);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }




    }
}
