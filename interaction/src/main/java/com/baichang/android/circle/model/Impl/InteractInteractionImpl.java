package com.baichang.android.circle.model.Impl;

import android.app.Application;
import com.baichang.android.circle.common.InteractionAPI;
import com.baichang.android.circle.common.InteractionAPIWrapper;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.entity.InteractionCommentList;
import com.baichang.android.circle.entity.InteractionCommentReplyList;
import com.baichang.android.circle.entity.InteractionDetailData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionNumberData;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.circle.entity.InteractionShareData;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.entity.InteractionUserData;
import com.baichang.android.circle.entity.InteractionUserInfo;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.baichang.android.request.UploadUtils;
import com.google.gson.Gson;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractInteractionImpl implements InteractInteraction {

  @Override
  public void cancel(int key) {

  }

  // 互动列表
  @Override
  public void getInteractionList(int typeId, int nowPage, final BaseListener<List<InteractionListData>> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("typeId", String.valueOf(typeId));
    map.put("nowPage", String.valueOf(nowPage));
    InteractionAPIWrapper.getInstance().getTrendsList(map)
        .compose(HttpSubscriber.<List<InteractionListData>>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<List<InteractionListData>>() {
          @Override
          public void success(List<InteractionListData> interactionListData) {
            listener.success(interactionListData);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 互动详情
  @Override
  public void getInteractionDetail(int id, final BaseListener<InteractionDetailData> listener) {
    InteractionUserData userData = InteractionConfig.getInstance().getUser();
    Map<String, String> map = new HashMap<>();
    map.put("id", String.valueOf(id));
    map.put("userId", userData.id);
    InteractionAPIWrapper.getInstance()
        .getTrendsDetail(map)
        .compose(HttpSubscriber.<InteractionDetailData>applySchedulers())
        .subscribe(new HttpSubscriber<>(
            new HttpSuccessListener<InteractionDetailData>() {
              @Override
              public void success(InteractionDetailData data) {
                listener.success(data);
              }
            }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 发表互动
  @Override
  public void publishImage(Application application, final String title, final String content,
      final String modelId, final List<String> paths, final BaseListener<Boolean> listener) {
    final InteractionUserData userData = InteractionConfig.getInstance().getUser();
    String[] imageArray = new String[paths.size()];
    Tiny.getInstance().init(application);
    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
    Tiny.getInstance().source(paths.toArray(imageArray)).batchAsFile().withOptions(options).batchCompress(
        new FileBatchCallback() {
          @Override
          public void callback(boolean isSuccess, String[] outfile) {
            final Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("content", content);
            map.put("typeId", modelId);
            map.put("hostName", userData.name);
            map.put("hostIcon", userData.avatar);
            map.put("address", userData.address);
            map.put("userId", userData.id);
            InteractionAPI api = new InteractionAPIWrapper();
            api.uploads(UploadUtils.getMultipartBodysForPath(Arrays.asList(outfile)))
                .compose(HttpSubscriber.<List<String>>applySchedulers())
                .flatMap(new Func1<List<String>, Observable<Boolean>>() {
                  @Override
                  public Observable<Boolean> call(List<String> list) {
                    map.put("images", new Gson().toJson(list));
                    return InteractionAPIWrapper.getInstance().publish(map)
                        .compose(HttpSubscriber.<Boolean>applySchedulers());
                  }
                })
                .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
                  @Override
                  public void success(Boolean aBoolean) {
                    listener.success(aBoolean);
                  }
                }, new HttpErrorListener() {
                  @Override
                  public void error(Throwable t) {
                    listener.error(t.getMessage());
                  }
                }));
          }
        });
  }

  // 发表互动 不带图片
  @Override
  public void publishNoImage(String title, String content, String modelId, final BaseListener<Boolean> listener) {
    InteractionUserData userDat = InteractionConfig.getInstance().getUser();
    final Map<String, String> map = new HashMap<>();
    map.put("title", title);
    map.put("content", content);
    map.put("typeId", modelId);
    map.put("hostName", userDat.name);
    map.put("hostIcon", userDat.avatar);
    map.put("address", userDat.address);
    map.put("userId", userDat.id);
    InteractionAPIWrapper.getInstance()
        .publish(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 回复的互动
  @Override
  public void getReplay(int nowPage, String userId, final BaseListener<List<InteractionReplyData>> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId);
    map.put("nowPage", String.valueOf(nowPage));
    InteractionAPIWrapper.getInstance()
        .getReply(map)
        .compose(HttpSubscriber.<List<InteractionReplyData>>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<List<InteractionReplyData>>() {
          @Override
          public void success(List<InteractionReplyData> list) {
            listener.success(list);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 互动类别
  @Override
  public void getInteractionTypeList(final BaseListener<List<InteractionTypeData>> listener) {
    InteractionAPIWrapper.getInstance()
        .getTrendsType()
        .compose(HttpSubscriber.<List<InteractionTypeData>>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<List<InteractionTypeData>>() {
          @Override
          public void success(List<InteractionTypeData> list) {
            listener.success(list);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 赞/取消赞
  @Override
  public void praise(int id, final BaseListener<Boolean> listener) {
    InteractionUserData userData = InteractionConfig.getInstance().getUser();
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", String.valueOf(id));
    map.put("userId", userData.id);
    InteractionAPIWrapper.getInstance()
        .praise(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 收藏/取消收藏
  @Override
  public void collect(int id, final BaseListener<Boolean> listener) {
    InteractionUserData userData = InteractionConfig.getInstance().getUser();
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", String.valueOf(id));
    map.put("userId", userData.id);
    InteractionAPIWrapper.getInstance()
        .collect(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 获取动态
  @Override
  public void getDynamics(String userId, int nowPage, final BaseListener<List<InteractionListData>> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId);
    map.put("nowPage", String.valueOf(nowPage));
    InteractionAPIWrapper.getInstance()
        .getDynamics(map)
        .compose(HttpSubscriber.<List<InteractionListData>>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<List<InteractionListData>>() {
          @Override
          public void success(List<InteractionListData> list) {
            listener.success(list);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  @Override
  public void getCollect(int nowPage, final BaseListener<List<InteractionListData>> listener) {
    InteractionUserData userData = InteractionConfig.getInstance().getUser();
    Map<String, String> map = new HashMap<>();
    map.put("userId", userData.id);
    map.put("nowPage", String.valueOf(nowPage));
    InteractionAPIWrapper.getInstance()
        .getCollect(map)
        .compose(HttpSubscriber.<List<InteractionListData>>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<List<InteractionListData>>() {
          @Override
          public void success(List<InteractionListData> list) {
            listener.success(list);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 删除互动
  @Override
  public void delete(int id, final BaseListener<Boolean> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("id", String.valueOf(id));
    InteractionAPIWrapper.getInstance()
        .delete(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 动态、回复、收藏数量
  @Override
  public void getNumbers(String userId, final BaseListener<InteractionNumberData> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId);
    InteractionAPIWrapper.getInstance()
        .getNumbers(map)
        .compose(HttpSubscriber.<InteractionNumberData>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<InteractionNumberData>() {
          @Override
          public void success(InteractionNumberData data) {
            listener.success(data);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 举报
  @Override
  public void report(int id, final BaseListener<Boolean> listener) {
    InteractionUserData userDa = InteractionConfig.getInstance().getUser();
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", String.valueOf(id));
    map.put("userId", userDa.id);
    InteractionAPIWrapper.getInstance()
        .report(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 互动评论
  @Override
  public void comment(int trendsId, InteractionCommentList commentData, final BaseListener<Boolean> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", String.valueOf(trendsId));
    map.put("commentUserId", commentData.userId);
    map.put("commentName", commentData.name);
    map.put("commentIcon", commentData.avatar);
    map.put("commentContent", commentData.content);
    InteractionAPIWrapper.getInstance()
        .comment(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 互动回复
  @Override
  public void reply(InteractionCommentReplyList replyData, final BaseListener<Boolean> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", String.valueOf(replyData.trendsId));
    map.put("commentId", String.valueOf(replyData.commentId));
    map.put("commentUserId", replyData.commentUserId);
    map.put("commentName", replyData.commentName);
    map.put("replayContent", replyData.replayContent);
    map.put("replayName", replyData.replayName);
    map.put("replayUserId", replyData.replayUserId);
    map.put("replayId", replyData.id);
    InteractionAPIWrapper.getInstance()
        .reply(map)
        .compose(HttpSubscriber.<Boolean>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<Boolean>() {
          @Override
          public void success(Boolean aBoolean) {
            listener.success(aBoolean);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 获取用户信息
  @Override
  public void getUserInfo(String userId, final BaseListener<InteractionUserInfo> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId);
    InteractionAPIWrapper.getInstance()
        .getUserInfo(map)
        .compose(HttpSubscriber.<InteractionUserInfo>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<InteractionUserInfo>() {
          @Override
          public void success(InteractionUserInfo info) {
            listener.success(info);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }

  // 获取分享连接
  @Override
  public void getShareLink(String id, final BaseListener<String> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("trendsId", id);
    InteractionAPIWrapper.getInstance()
        .getShareLink(map)
        .compose(HttpSubscriber.<InteractionShareData>applySchedulers())
        .subscribe(new HttpSubscriber<>(new HttpSuccessListener<InteractionShareData>() {
          @Override
          public void success(InteractionShareData interactionShareData) {
            listener.success(interactionShareData.link);
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable t) {
            listener.error(t.getMessage());
          }
        }));
  }
}
