package com.dy.igserver.api;

import com.dy.igserver.utils.RJson;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/friends")
public class FriendsMessageController {
    @ApiOperation("测试")
    @PostMapping("/test")
    public RJson getOfflineReadedAllFriendsMessage(@RequestParam String user_id) {
        return null;
    }
}
