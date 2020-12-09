package desafioIBM.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import desafioIBM.dto.LoginDTO;
import desafioIBM.model.Role;
import desafioIBM.service.GitHubService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import desafioIBM.dto.UserResponseDTO;
import desafioIBM.service.UserService;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private GitHubService gitHubService;

  @PostMapping("/auth")
  @ApiOperation(value = "${UserController.auth}")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public String login(@Valid @RequestBody LoginDTO login) {
    return userService.signin(login.getUsername(), login.getPassword());
  }

  @GetMapping(value = "/find")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "${UserController.find}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied"),
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public ResponseEntity<String> allRepositoriesByUserGit(@ApiParam("userGit") @RequestParam String userGit, HttpServletRequest req) throws Exception {

    String response = "";

    UserResponseDTO userResponse = modelMapper.map(userService.valideUser(req), UserResponseDTO.class);

    if(userResponse.getRoles().contains(Role.ROLE_ADMIN) || userResponse.getRoles().contains(Role.ROLE_CLIENT)) {
      response = gitHubService.findReposotoryByUser(userGit);
    }

    if (!response.isEmpty())
      return ResponseEntity.ok(response);

    return ResponseEntity.notFound().build();
  }
}
