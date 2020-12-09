package desafioIBM;

import java.util.ArrayList;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import desafioIBM.model.Role;
import desafioIBM.model.User;
import desafioIBM.service.UserService;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Override
  public void run(String... params) throws Exception {
    User admin = new User();
    admin.setUsername("admin");
    admin.setPassword("12345");
    admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));
    userService.createUser(admin);

    User client = new User();
    client.setUsername("client");
    client.setPassword("12345");
    client.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));
    userService.createUser(client);
  }

}
