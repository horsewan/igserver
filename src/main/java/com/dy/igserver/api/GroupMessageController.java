package com.dy.igserver.api;


import com.dy.igserver.logic.group.GroupMessager;
import com.dy.igserver.utils.RJson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@RestController
@RequestMapping("/group")
public class GroupMessageController {

    private static Logger logger = LoggerFactory.getLogger(GroupMessageController.class);

    /*@ApiOperation("拉取离线群消息")
    @ApiImplicitParam(name="group_id",value="群消息",required = true,dataType="String")
    @PostMapping("/getgroupmsg")
    public RJson getGroupMsg(@RequestParam String group_id) {
        if (group_id.isEmpty()) {
            logger.error("群号不能为空");
            return RJson.error().setCode(2);
        }
        return  RJson.ok().setCode(1).setData("");
    }*/

    @ApiOperation("创建群,返回群消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_id",value="创建群的群主的用户id",required = true,dataType="String"),
            @ApiImplicitParam(name="friends",value="好友id,用逗号隔开,最少要有两个人",required = true,dataType="String"),
    })
    @PostMapping("/creategroup")
    public RJson createGroup(@RequestParam String user_id,@RequestParam String friends) {
        if (user_id.isEmpty()) {
            logger.error("用户id不能为空");
            return RJson.error().setCode(2).setMsg("用户id不能为空");
        }
        String [] vfriends =null;
        try{
            vfriends =friends.split(",");
            if(vfriends.length<2){
                return RJson.error().setCode(2).setMsg("用户数量太少，不适合建群");
            }
        }catch (Exception e){
            return RJson.error().setCode(2).setMsg("参数格式错误");
        }

        Document doc=GroupMessager.addGroup(user_id,vfriends);
        return  RJson.ok().setCode(1).setData(doc).setMsg("成功加群");
    }

    @ApiOperation("查询群信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群ID",required = true,dataType="String"),
    })
    @PostMapping("/findgroup")
    public RJson findGroup(@RequestParam String group_id) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2).setMsg("群id不能为空");
        }
        //Bson afilter = Filters.eq("user_id",user_id);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("修改群名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="创建群的群主的用户id",required = true,dataType="String"),
            @ApiImplicitParam(name="group_name",value="群的名称",required = true,dataType="String"),
    })
    @PostMapping("/updategroupname")
    public RJson updateGroupName(@RequestParam String group_id,@RequestParam String group_name) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.updateGroupName(group_id,group_name);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("修改群url")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群ID",required = true,dataType="String"),
            @ApiImplicitParam(name="group_url",value="群的url",required = true,dataType="String"),
    })
    @PostMapping("/updategroupurl")
    public RJson updateGroupUrl(@RequestParam String group_id,@RequestParam String group_url) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.updateGroupUrl(group_id,group_url);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }


    @ApiOperation("修改群公告-如果有就修改,如果没有就添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="创建群的群主的用户id",required = true,dataType="String"),
            @ApiImplicitParam(name="notice",value="群的公告",required = true,dataType="String"),
    })
    @PostMapping("/updategroupnotice")
    public RJson updateGroupNotice(@RequestParam String group_id,@RequestParam String notice) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.updateGroupNotice(group_id,notice);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }


    @ApiOperation("加入群聊")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="friends",value="好友id,用逗号隔开,最少要有两个人",required = true,dataType="String"),
    })
    @PostMapping("/joingroup")
    public RJson joinGroup(@RequestParam String group_id,@RequestParam String friends) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        String [] vfriends =null;
        try{
            vfriends =friends.split(",");
            if(vfriends.length<1){
                return RJson.error().setCode(2).setMsg("用户数量太少，不适合加入");
            }
        }catch (Exception e){
            return RJson.error().setCode(2).setMsg("参数格式错误");
        }
        GroupMessager.joinGroup(group_id,vfriends);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("移出群聊")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="friends",value="好友id,用逗号隔开,最少要有两个人",required = true,dataType="int"),
    })
    @PostMapping("/removegroup")
    public RJson removeGroup(@RequestParam String group_id,@RequestParam String friends) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        String [] vfriends =null;
        try{
            vfriends =friends.split(",");
            if(vfriends.length<1){
                return RJson.error().setCode(2).setMsg("用户数量太少，不适合加入");
            }
        }catch (Exception e){
            return RJson.error().setCode(2).setMsg("参数格式错误");
        }
        GroupMessager.removeGroup(group_id,vfriends);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("更改群主")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="user_id",value="用户id",required = true,dataType="String"),
    })
    @PostMapping("/changegroupadmin")
    public RJson changeGroupAdmin(@RequestParam String group_id,@RequestParam String user_id) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.updateGroupAdmin(group_id,user_id);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("退群")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="user_id",value="用户id",required = true,dataType="String"),
    })
    @PostMapping("/exitgroup")
    public RJson exitGroup(@RequestParam String group_id,@RequestParam String user_id) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.exitGroup(group_id,user_id);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));
    }

    @ApiOperation("个人修改群昵称")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="user_id",value="用户id",required = true,dataType="String"),
            @ApiImplicitParam(name="nickname",value="群昵称",required = true,dataType="String"),
    })
    @PostMapping("/updategroupusernickname")
    public RJson updateGroupUserNickName(@RequestParam String group_id,@RequestParam String user_id,@RequestParam String nickname) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        GroupMessager.updateGroupUserNickName(group_id,user_id,nickname);
        return  RJson.ok().setCode(1).setData(GroupMessager.getGroupByid(group_id));

    }

    @ApiOperation("删除群-群主才可以删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="group_id",value="群id",required = true,dataType="String"),
            @ApiImplicitParam(name="user_id",value="用户id",required = true,dataType="String"),
    })
    @PostMapping("/deletegroup")
    public RJson deleteGroup(@RequestParam String group_id,@RequestParam String user_id) {
        if (group_id.isEmpty()) {
            logger.error("群id不能为空");
            return RJson.error().setCode(2);
        }
        int code=GroupMessager.deleteGroup(group_id,user_id);
        if(code>1) {
            return  RJson.error().setCode(2).setMsg("删除失败，可能是你没有权限或者服务器错误");
        }else return  RJson.ok().setCode(1).setMsg("删除成功");
    }


    @ApiOperation("查询用户自己所有的群")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_id",value="用户id",required = true,dataType="String"),
    })
    @PostMapping("/getusergroup")
    public RJson getUserGroup(@RequestParam String user_id) {

      return  RJson.ok().setCode(1).setMsg("查询成功").setData(GroupMessager.findUserGroup(user_id));
    }

   /* @ApiOperation("发送群消息" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="msg",value="消息内容",required = true,dataType="String"),
            @ApiImplicitParam(name="typeu",value="消息类型",required = true,dataType="String"),
    })
    @PostMapping("/imsysmsg")
    public RJson sendGroupMsg(@RequestParam String group_id,@RequestParam String msg, @RequestParam int typeu) {
        return RJson.ok().setData("已发送 ["+msg+"] to -> All User");
    }*/

}
