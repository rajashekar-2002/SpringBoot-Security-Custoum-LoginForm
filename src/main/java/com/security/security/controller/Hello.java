package com.security.security.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.security.security.repo.StudentRepo;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class Hello {
    @Autowired
    private StudentRepo studentRepo;


    private List<Student> students=new ArrayList<>(List.of(
        new Student(1,"raju","js"),
        new Student(2,"chotu","react"),
        new Student(3,"ganiger","spring")
        ));


    @GetMapping("/")
    public String hello(HttpServletRequest request){
        return "hello" + request + request.getSession().getId();
    }

    // @GetMapping("/all")
    // public List<Student> getall(){

    //     return students;
    // }

    @PostMapping("/add")
    public List<Student> add(@RequestBody Student student){
        students.add(student);
        return students;
    }

    @GetMapping("/getCsrf")
    public CsrfToken getcsrf(HttpServletRequest http){
        return (CsrfToken) http.getAttribute("_csrf");
    }

    @PostMapping("/temp")
    public List<Student> add(){

        return students;
    }

    // @GetMapping("/all")
    // public List<Student> database(){
    //     return studentRepo.findAll();
    // }

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/password")
    public List<Student> updatePassword(){

        Student object=studentRepo.findByName("raju");
        object.setPassword(passwordEncoder.encode("123"));
        studentRepo.save(object);
        return studentRepo.findAll();
    }



    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/all")
    public String all(){
        return "all";
    }





}



