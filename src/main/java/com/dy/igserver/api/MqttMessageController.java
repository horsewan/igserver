package com.dy.igserver.api;

import com.dy.igserver.utils.MQUtil;
import com.dy.igserver.utils.RJson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/mqtt")
public class MqttMessageController {
    private static Logger logger = LoggerFactory.getLogger(OfflineMessageController.class);
    @ApiOperation("通过mqtt推送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="topic",value="标题",required = true,dataType="String"),
            @ApiImplicitParam(name="msg",value="消息",required = true,dataType="String"),
    })
    @PostMapping("/sendmsg")
    public RJson getOfflineAllFriendsMessage(@RequestParam String topic,@RequestParam String msg) {
        MqttClient mqc= MQUtil.getClient("serverClient");
        MQUtil.mqttSendMessage(mqc,topic,msg);
        return RJson.ok().setMsg("推送主题消息成功");
    }
}
