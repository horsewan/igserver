package com.dy.igserver.api;


import com.dy.igserver.logic.offline.OffLineMessager;
import com.dy.igserver.utils.RJson;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


@EnableSwagger2
@RestController
@RequestMapping("/offline")
public class OfflineMessageController {

    private static Logger logger = LoggerFactory.getLogger(OfflineMessageController.class);

    @ApiOperation("拉取用户所有的离线消息")
    @PostMapping("/offlineallmsg")
    public RJson getOfflineAllFriendsMessage(@RequestParam String user_id) {

        List<Document> rDocument= OffLineMessager.getReceiveMsg(user_id,0);
        OffLineMessager.updateReceiveMsg(user_id);
        return RJson.ok().setData(rDocument);

    }

    @ApiOperation("拉取用户某个好友的离线消息")
    @PostMapping("/offlinefriendmsg")
    public RJson getOfflineFriendMessage(@RequestParam String user_id,@RequestParam String user_friend_id) {

        List<Document> rDocument = OffLineMessager.getFriendReceiveMsg(user_id,user_friend_id,0);
        OffLineMessager.updateReceiveMsg(user_id,user_friend_id);
        return RJson.ok().setData(rDocument);

    }


    @ApiOperation("拉取用户所有已读的离线消息")
    @PostMapping("/offlineallmsgreaded")
    public RJson getOfflineReadedAllFriendsMessage(@RequestParam String user_id) {

        List<Document> rDocument= OffLineMessager.getReceiveMsg(user_id,1);
        return RJson.ok().setData(rDocument);

    }

    @ApiOperation("拉取用户某个好友的已经读取的离线消息")
    @PostMapping("/offlinefriendmsgreaded")
    public RJson getOfflineReadedFriendMessage(@RequestParam String user_id,@RequestParam String user_friend_id) {

        List<Document> rDocument = OffLineMessager.getFriendReceiveMsg(user_id,user_friend_id,1);
        return RJson.ok().setData(rDocument);

    }


}
