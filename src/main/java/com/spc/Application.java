package com.spc;

import com.spc.annotation.EnableSpcClients;
import com.spc.test.MyTest;
import com.spc.test.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@EnableSpcClients(basePackageClasses = MyTest.class)
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    private MyTest myTest;

    @Resource
    private User myTest1;

    @Override
    public void run(String... args) throws Exception {
        List<String> data = myTest.findUserById("321");
        List<String> data1 = myTest1.findNameById("01");
        System.out.println(data);
        System.out.println(data1);

    }
}
