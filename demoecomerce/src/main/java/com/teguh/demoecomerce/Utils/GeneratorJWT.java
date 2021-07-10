package com.teguh.demoecomerce.Utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class GeneratorJWT {

    public static String createToken(String id) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                // Set Header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //set payload or claim
                .setId(id)
                .setIssuedAt(now)
                .setSubject("ecommerce")
                .setIssuer("PT.BUANA")
//                  .setExpiration(new Date(System.currentTimeMillis()+1*60*1000))// 1 menit, 60 * 60 * 1000 = 30 menit
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) //24 Jam, 60*60*2*1000 = 2 jam
                .signWith(SignatureAlgorithm.HS256, "chart");

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims validateToken(String token) {
        Claims claims = null;

        claims = Jwts.parser()
                .setSigningKey("chart")
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }



}
