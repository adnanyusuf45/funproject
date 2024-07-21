package com.funproject.funproject.handler;

import com.funproject.funproject.entity.User;
import com.funproject.funproject.model.ResponseModel;
import com.funproject.funproject.repository.UserRepository;
import com.funproject.funproject.request.LoginUserRequest;
import com.funproject.funproject.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {

    @Autowired
    UserRepository userRepository;

    public Mono<ServerResponse> registerUser (ServerRequest request){

        try {

            return request.bodyToMono(RegisterUserRequest.class)
                    .flatMap(bodyData -> {

                        if(bodyData.getUsername().trim().isEmpty() || bodyData.getPassword().trim().isEmpty()){

                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10003")
                                    .status("gagal register user")
                                    .respDescription("Username dan / atau password kosong")
                                    .build();
                            return ServerResponse.status(HttpStatus.BAD_REQUEST).body(Mono.just(responseModel),ResponseModel.class);
                        }

                        User usernameEntry = userRepository.findByUsername(bodyData.getUsername().trim());
                        if(usernameEntry != null){
                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10002")
                                    .status("gagal register user")
                                    .respDescription("Username sudah terpakai")
                                    .build();

                            return ServerResponse.status(HttpStatus.CONFLICT).body(Mono.just(responseModel), ResponseModel.class);
                        }

                        userRepository.save(User.builder()
                                .username(bodyData.getUsername().trim())
                                .password(bodyData.getPassword().trim())
                                .build());

                        ResponseModel responseModel = ResponseModel.builder()
                                .respCode("10000")
                                .status("sukses register user")
                                .respDescription("Registrasi Berhasil")
                                .build();


                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(responseModel), ResponseModel.class);
                    });
        }catch (Exception e){

            ResponseModel responseModel = ResponseModel.builder()
                    .respCode("10001")
                    .status("gagal register user")
                    .respDescription("Internal Server Error")
                    .build();

            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel), ResponseModel.class);
        }

    }

    public Mono<ServerResponse> loginUser (ServerRequest request){

        try {

            return request.bodyToMono(LoginUserRequest.class)
                    .flatMap(bodyData -> {
                        if(bodyData.getUsername().isEmpty() || bodyData.getPassword().isEmpty()){

                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10003")
                                    .status("gagal login user")
                                    .respDescription("Username dan / atau password kosong")
                                    .build();
                            return ServerResponse.status(HttpStatus.BAD_REQUEST).body(Mono.just(responseModel),ResponseModel.class);
                        }

                        User usernameEntry = userRepository.findByUsername(bodyData.getUsername().trim());

                        if(usernameEntry == null || !usernameEntry.getPassword().equals(bodyData.getPassword().trim())){
                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10002")
                                    .status("gagal login user")
                                    .respDescription("Username / Password Tidak Cocok")
                                    .build();

                            return ServerResponse.status(HttpStatus.UNAUTHORIZED).body(Mono.just(responseModel), ResponseModel.class);
                        }

                        ResponseModel responseModel = ResponseModel.builder()
                                .respCode("10000")
                                .status("sukses login user")
                                .respDescription("Registrasi Berhasil")
                                .build();


                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(responseModel), ResponseModel.class);

                    });

/*            if(user.getUsername().trim()==null ||user.getUsername().trim().equals("") || user.getPassword().trim()==null||user.getPassword().trim().equals("")){

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username dan / atau password kosong");

            }


            User usernameEntry = userRepository.findByUsername(user.getUsername().trim());

            if(usernameEntry == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username / Password Tidak Cocok");
            }

            if(!usernameEntry.getPassword().equals(user.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username / Password Tidak Cocok");

            }

            return ResponseEntity.status(HttpStatus.OK).body("Sukses Login");*/

        }catch (Exception e){

            ResponseModel responseModel = ResponseModel.builder()
                    .respCode("10001")
                    .status("gagal login user")
                    .respDescription("Internal Server Error")
                    .build();

            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel), ResponseModel.class);
        }

    }
}
