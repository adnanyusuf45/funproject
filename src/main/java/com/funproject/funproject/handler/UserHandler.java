package com.funproject.funproject.handler;

import com.funproject.funproject.entity.User;
import com.funproject.funproject.model.ResponseModel;
import com.funproject.funproject.repository.UserRepository;
import com.funproject.funproject.request.EditUserRequest;
import com.funproject.funproject.request.LoginUserRequest;
import com.funproject.funproject.request.RegisterUserRequest;
import com.funproject.funproject.response.ListUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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

        }catch (Exception e){

            ResponseModel responseModel = ResponseModel.builder()
                    .respCode("10001")
                    .status("gagal login user")
                    .respDescription("Internal Server Error")
                    .build();

            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel), ResponseModel.class);
        }

    }

    public Mono<ServerResponse> listUser (ServerRequest request){

        try {

            List<User> listUsernameEntry = userRepository.findAll();

            ListUserResponse listUserResponse = ListUserResponse.builder()
                    .respCode("10000")
                    .status("Sukses get list user")
                    .respDescription("get list user success")
                    .data(listUsernameEntry)
                    .build();

            return ServerResponse.ok().body(Mono.just(listUserResponse),ListUserResponse.class);

        }catch (Exception e){

            ResponseModel responseModel = ResponseModel.builder()
                    .respCode("10001")
                    .status("gagal get list user")
                    .respDescription("Internal Server Error")
                    .build();

            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel), ResponseModel.class);
        }

    }

    public Mono<ServerResponse> editUser (ServerRequest request){

        try {
            return request.bodyToMono(EditUserRequest.class)
                    .flatMap(bodyData -> {

                        if(bodyData.getUsername().trim()==null ||bodyData.getUsername().trim().equals("") || bodyData.getPassword().trim()==null||bodyData.getPassword().trim().equals("")){

                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10001")
                                    .status("gagal edit user")
                                    .respDescription("Internal Server Error")
                                    .build();
                            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel),ResponseModel.class);
                        }

                        Optional<User> userDatabyId = userRepository.findById(Long.valueOf(request.pathVariable("id")));

                        if (userDatabyId.isPresent()) {

                            if(userDatabyId.get().getUsername().trim().equals(bodyData.getUsername().trim())){

                                if(userDatabyId.get().getPassword().trim().equals(bodyData.getPassword().trim())){

                                    ResponseModel responseModel = ResponseModel.builder()
                                            .respCode("10002")
                                            .status("gagal edit user")
                                            .respDescription("Password tidak boleh sama dengan password sebelumnya")
                                            .build();

                                    return ServerResponse.status(HttpStatus.BAD_REQUEST).body(Mono.just(responseModel), ResponseModel.class);
                                }

                                User _user = userDatabyId.get();
                                _user.setUsername(bodyData.getUsername().trim());
                                _user.setPassword(bodyData.getPassword().trim());
                                userRepository.save(_user);

                                ResponseModel responseModel = ResponseModel.builder()
                                        .respCode("10000")
                                        .status("sukses edit user")
                                        .respDescription("Edit User Berhasil")
                                        .build();

                                return ServerResponse.status(HttpStatus.CREATED).body(Mono.just(responseModel), ResponseModel.class);

                            }else{

                                User usernameDatacheck = userRepository.findByUsername(bodyData.getUsername().trim());

                                if(usernameDatacheck == null){
                                    User _user = userDatabyId.get();
                                    _user.setUsername(bodyData.getUsername().trim());
                                    _user.setPassword(bodyData.getPassword().trim());
                                    userRepository.save(_user);

                                    ResponseModel responseModel = ResponseModel.builder()
                                            .respCode("10000")
                                            .status("sukses edit user")
                                            .respDescription("Edit User Berhasil")
                                            .build();

                                    return ServerResponse.status(HttpStatus.CREATED).body(Mono.just(responseModel), ResponseModel.class);
                                }else{
                                    ResponseModel responseModel = ResponseModel.builder()
                                            .respCode("10003")
                                            .status("gagal edit user")
                                            .respDescription("Username sudah terpakai")
                                            .build();

                                    return ServerResponse.status(HttpStatus.CONFLICT).body(Mono.just(responseModel), ResponseModel.class);
                                }

                            }

                        } else {
                            ResponseModel responseModel = ResponseModel.builder()
                                    .respCode("10001")
                                    .status("gagal edit user")
                                    .respDescription("Internal Server Error")
                                    .build();
                            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel),ResponseModel.class);
                        }

                    });
        }catch (Exception e){
            ResponseModel responseModel = ResponseModel.builder()
                    .respCode("10001")
                    .status("gagal edit user")
                    .respDescription("Internal Server Error")
                    .build();
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(responseModel),ResponseModel.class);
        }

    }



}
