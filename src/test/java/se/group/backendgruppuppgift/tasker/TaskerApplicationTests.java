package se.group.backendgruppuppgift.tasker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskerApplicationTests {

    @Autowired
    UserService service;

    @Test
	public void contextLoads() {



		User user = service.findLastUser();
        System.out.println(user.toString());


	}

}
