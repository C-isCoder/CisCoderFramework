package com.baichang.android.circle.model.Impl;

import com.baichang.android.circle.entity.InteractionCommentData;
import com.baichang.android.circle.entity.InteractionCommentListData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionOtherReportData;
import com.baichang.android.circle.InteractInteraction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractInteractionImpl implements InteractInteraction {

  @Override
  public void cancel(int key) {

  }


  @Override
  public void getInteractionList(int nowPage, BaseListener<List<InteractionListData>> listener) {
    List<InteractionListData> list = new ArrayList<>();
    List<String> imageList = new ArrayList<>();
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    imageList.add("");
    InteractionListData data = new InteractionListData();
    data.id = 1;
    data.name = "你的名字";
    data.avatar = "";
    data.commentCount = 1000;
    data.praiseCount = 10101;
    data.content = "据说这个是一个测试的内容，然后就没有然后了。随便写点什么东西凑一下字数。看看这个间距是不是合适。嗯，就是这样，没啥了。";
    data.time = "2017-03-23";
    data.title = "标题不知道会不会超过一行，超过一行咋办，结尾用....吧。";
    data.images = imageList;
    InteractionListData data1 = new InteractionListData();
    data1.id = 2;
    data1.name = "徐老二";
    data1.avatar = "";
    data1.commentCount = 1000;
    data1.praiseCount = 10101;
    data1.content = "据说这个是一个测试的内容，然后就没有然后了。随便写点什么东西凑一下字数。看看这个间距是不是合适。嗯，就是这样，没啥了。";
    data1.time = "2017-03-23";
    data1.title = "标题不知道会不会超过一行，超过一行咋办，结尾用....吧。";
    data1.images = imageList;
    InteractionListData data2 = new InteractionListData();
    data2.id = 3;
    data2.name = "who am i";
    data2.avatar = "";
    data2.commentCount = 1000;
    data2.praiseCount = 10101;
    data2.content = "据说这个是一个测试的内容，然后就没有然后了。随便写点什么东西凑一下字数。看看这个间距是不是合适。嗯，就是这样，没啥了。";
    data2.time = "2017-03-23";
    data2.title = "标题不知道会不会超过一行，超过一行咋办，结尾用....吧。";
    data2.images = imageList;
    InteractionListData data3 = new InteractionListData();
    data3.id = 4;
    data3.name = "where are you";
    data3.avatar = "";
    data3.commentCount = 1000;
    data3.praiseCount = 10101;
    data3.content = "据说这个是一个测试的内容，然后就没有然后了。随便写点什么东西凑一下字数。看看这个间距是不是合适。嗯，就是这样，没啥了。";
    data3.time = "2017-03-23";
    data3.title = "标题不知道会不会超过一行，超过一行咋办，结尾用....吧。";
    data3.images = imageList;
    InteractionListData data4 = new InteractionListData();
    data4.id = 5;
    data4.name = "coding";
    data4.avatar = "";
    data4.commentCount = 1000;
    data4.praiseCount = 10101;
    data4.content = "据说这个是一个测试的内容，然后就没有然后了。随便写点什么东西凑一下字数。看看这个间距是不是合适。嗯，就是这样，没啥了。";
    data4.time = "2017-03-23";
    data4.title = "标题不知道会不会超过一行，超过一行咋办，结尾用....吧。";
    data4.images = imageList;
    list.add(data);
    list.add(data1);
    list.add(data2);
    list.add(data3);
    list.add(data4);
    listener.success(list);
  }

  @Override
  public void getInteractionDetail(int id, BaseListener<List<InteractionCommentData>> listener) {
    List<InteractionCommentData> list = new ArrayList<>();
    InteractionCommentListData data1 = new InteractionCommentListData();
    InteractionCommentListData data2 = new InteractionCommentListData();
    InteractionCommentListData data3 = new InteractionCommentListData();
    InteractionCommentListData data4 = new InteractionCommentListData();
    data1.id = 1;
    data1.content = "楼主你好";
    data1.reportName = "路人甲";
    data1.owner = "你的名字";

    data2.id = 2;
    data2.owner = "你的名字";
    data2.content = "楼主发话了";
    data2.reportName = "";

    data3.id = 3;
    data3.owner = "你的名字";
    data3.reportName = "路人乙";
    data3.content = "楼主你多大了";

    data4.id = 4;
    data4.owner = "你的名字";
    data4.reportName = "";
    data4.content = "楼主又说话了";

    InteractionCommentData commentData = new InteractionCommentData();
    commentData.avatar = "";
    commentData.comments = new ArrayList<>();
    Collections.addAll(commentData.comments, data1, data2, data3, data4);
    commentData.time = "2017-03-24 09:28:32";
    commentData.content = "互动的内容";
    commentData.id = 1;
    commentData.name = "你的名字";
    list.add(commentData);

    InteractionCommentListData data5 = new InteractionCommentListData();
    InteractionCommentListData data6 = new InteractionCommentListData();
    InteractionCommentListData data7 = new InteractionCommentListData();
    InteractionCommentListData data8 = new InteractionCommentListData();
    data5.id = 1;
    data5.content = "楼主你好，你吃饭了么";
    data5.reportName = "路人丙";
    data5.owner = "她的名字";

    data6.id = 2;
    data6.owner = "她的名字";
    data6.content = "楼主你是谁？";
    data6.reportName = "";

    data7.id = 3;
    data7.owner = "她的名字";
    data7.reportName = "路人11";
    data7.content = "楼主是个SB";

    data8.id = 4;
    data8.owner = "她的名字";
    data8.reportName = "";
    data8.content = "楼主又说话了";

    InteractionCommentData commentData1 = new InteractionCommentData();
    commentData1.avatar = "";
    commentData1.comments = new ArrayList<>();
    Collections.addAll(commentData1.comments, data5, data6, data7, data8);
    commentData1.time = "2017-03-24 09:28:32";
    commentData1.content = "这是第二条内容加个电话试试17686616852";
    commentData1.id = 1;
    commentData1.name = "她的名字";
    list.add(commentData1);

    InteractionCommentListData data9 = new InteractionCommentListData();
    InteractionCommentListData data0 = new InteractionCommentListData();
    data9.id = 1;
    data9.content = "楼主你好，这个狗狗多打了。";
    data9.reportName = "路人丙";
    data9.owner = "她的名字";

    data0.id = 2;
    data0.owner = "狗的名字";
    data0.content = "哈士奇二哈吼吼吼吼吼吼吼吼吼哈哈哈哈哈。弄个电话试试";
    data0.reportName = "";

    InteractionCommentData commentData2 = new InteractionCommentData();
    commentData2.avatar = "";
    commentData2.comments = new ArrayList<>();
    Collections.addAll(commentData2.comments, data0, data9);
    commentData2.time = "2017-03-24 09:28:32";
    commentData2.content = "这个该是第三条了肌肤姐夫姐姐发几个卡换地方回家那绝对是个";
    commentData2.id = 1;
    commentData2.name = "狗的名字";
    list.add(commentData2);

    listener.success(list);
  }

  @Override
  public void publish(String title, String content, String modelId, List<String> paths,
      BaseListener<Boolean> listener) {
    listener.success(true);
  }

  @Override
  public void getMeInteraction(int nowPage, BaseListener<List<InteractionOtherReportData>> listener) {
    InteractionOtherReportData data = new InteractionOtherReportData();
    List<InteractionOtherReportData> list = new ArrayList<>();
    list.add(data);
    list.add(data);
    list.add(data);
    list.add(data);
    listener.success(list);
  }
}
