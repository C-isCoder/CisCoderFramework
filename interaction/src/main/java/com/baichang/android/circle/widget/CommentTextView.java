package com.baichang.android.circle.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import com.baichang.android.circle.R;
import com.baichang.android.circle.entity.InteractionCommentReplyList;
import com.baichang.android.utils.BCDensityUtil;
import java.lang.ref.WeakReference;

/**
 * Created by iCong on 2017/3/22.
 */

public class CommentTextView extends android.support.v7.widget.AppCompatTextView {

  private static final float mTextSize = 13;
  private static final float mPadding = 4f;
  private static final String REPORT_FLAG_TEXT = " 回复 ";
  private static final String COLON = ":";
  private static final String IMAGE_PLACEHOLDER = "image";
  private static final int COMMENT_CLICK = 1000;
  private static final int REPORT_CLICK = 1001;
  private static final int CONTENT_CLICK = 1002;
  private static final int REPORT_CLICK2 = 1003;
  private static final int OWNER_DRAWABLE_RES_ID = R.mipmap.interaction_icon_owner;

  public CommentTextView(Context context) {
    super(context);
    init();
  }

  public CommentTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CommentTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setTextSize(mTextSize);
    setTextColor(ContextCompat.getColor(getContext(), R.color.cm_tv_black1));
    setLineSpacing(2, 1);
    setPadding(0, getPadding(getContext()), 0, getPadding(getContext()));
    setAutoLinkMask(Linkify.PHONE_NUMBERS);
    setLinkTextColor(ContextCompat.getColor(getContext(), R.color.interaction_text_font));
    setGravity(Gravity.CENTER_VERTICAL);
    setMovementMethod(LinkMovementMethod.getInstance());
  }

  public void setText(InteractionCommentReplyList comment) {
    if (comment == null) {
      return;
    }
    setTag(comment);
    SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
    if (TextUtils.isEmpty(comment.replayName)) { // XXX: 内容
      // 楼主
      if (comment.commentType == 1) {
        // 内容
        stringBuilder.append(comment.commentName)
            .append(IMAGE_PLACEHOLDER)
            .append(COLON)
            .append(comment.replayContent);
        // 楼主图标
        stringBuilder.setSpan(new CommentImageSpan(), comment.commentName.length(),
            comment.commentName.length() + getPlaceHolderLength(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 评论人点击
        stringBuilder.setSpan(new CommentClickableSpan(COMMENT_CLICK, listener), 0,
            comment.commentName.length() + getPlaceHolderLength(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 内容点击
        stringBuilder.setSpan(
            new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
            comment.commentName.length() + getPlaceHolderLength() + getColonLength(),
            comment.commentName.length()
                + getPlaceHolderLength()
                + getColonLength()
                + comment.replayContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else {
        // 非楼主
        stringBuilder.append(comment.commentName).append(COLON).append(comment.replayContent);
        // 评论人点击
        stringBuilder.setSpan(new CommentClickableSpan(COMMENT_CLICK, listener), 0,
            comment.commentName.length() + getColonLength(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 内容点击
        stringBuilder.setSpan(
            new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
            comment.commentName.length() + getColonLength(),
            comment.commentName.length() + getColonLength() + comment.replayContent.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    } else { // XXX 回复 XXX: 内容
      if (comment.replayType == 1) {
        // 回复人是楼主
        // 内容
        stringBuilder.append(comment.replayName)
            .append(IMAGE_PLACEHOLDER)
            .append(REPORT_FLAG_TEXT)
            .append(comment.commentName)
            .append(COLON)
            .append(comment.replayContent);
        // 楼主图标
        stringBuilder.setSpan(new CommentImageSpan(), comment.replayName.length(),
            comment.replayName.length() + getPlaceHolderLength(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(REPORT_CLICK2, listener), 0,
            comment.replayName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 被回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(COMMENT_CLICK, listener),
            comment.replayName.length() + getPlaceHolderLength() + getReportLength(),
            comment.replayName.length()
                + getPlaceHolderLength()
                + getReportLength()
                + comment.commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 内容点击
        stringBuilder.setSpan(
            new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
            comment.replayName.length()
                + getPlaceHolderLength()
                + getReportLength()
                + comment.commentName.length()
                + getColonLength(), comment.replayName.length()
                + getPlaceHolderLength()
                + getReportLength()
                + comment.commentName.length()
                + getColonLength()
                + comment.replayContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (comment.commentType == 1) {
        // 被回复人是楼主
        // 内容
        stringBuilder.append(comment.replayName)
            .append(REPORT_FLAG_TEXT)
            .append(comment.commentName)
            .append(IMAGE_PLACEHOLDER)
            .append(COLON)
            .append(comment.replayContent);
        // 楼主图标
        stringBuilder.setSpan(new CommentImageSpan(),
            comment.replayName.length() + getReportLength() + comment.commentName.length(),
            comment.replayName.length()
                + getReportLength()
                + comment.commentName.length()
                + getPlaceHolderLength(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(REPORT_CLICK2, listener), 0,
            comment.replayName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 被回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(COMMENT_CLICK, listener),
            comment.replayName.length() + getReportLength(),
            comment.replayName.length() + getReportLength() + comment.commentName.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 内容点击
        stringBuilder.setSpan(
            new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
            comment.replayName.length()
                + getReportLength()
                + comment.commentName.length()
                + getReportLength()
                + getColonLength(), comment.replayName.length()
                + getReportLength()
                + comment.commentName.length()
                + getReportLength()
                + getColonLength()
                + comment.replayContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else {
        // 都不是楼主
        // 内容
        stringBuilder.append(comment.replayName)
            .append(REPORT_FLAG_TEXT)
            .append(comment.commentName)
            .append(COLON)
            .append(comment.replayContent);
        // 回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(REPORT_CLICK2, listener), 0,
            comment.replayName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 被回复人点击
        stringBuilder.setSpan(new CommentClickableSpan(COMMENT_CLICK, listener),
            comment.replayName.length() + getReportLength(),
            comment.replayName.length() + getReportLength() + comment.commentName.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 内容点击
        stringBuilder.setSpan(
            new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
            comment.replayName.length()
                + getReportLength()
                + comment.commentName.length()
                + getColonLength(), comment.replayName.length()
                + getReportLength()
                + comment.commentName.length()
                + getColonLength()
                + comment.replayContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
    setText(stringBuilder);
  }

  private int getPadding(Context context) {
    return BCDensityUtil.dip2px(context, mPadding);
  }

  private int getReportLength() {
    return REPORT_FLAG_TEXT.length();
  }

  private int getColonLength() {
    return COLON.length();
  }

  private int getPlaceHolderLength() {
    return IMAGE_PLACEHOLDER.length();
  }

  public InteractionCommentReplyList getCommentData() {
    return (InteractionCommentReplyList) getTag();
  }

  private CommentOnClickListener listener;

  public void setCommentOnClickListener(CommentOnClickListener listener) {
    this.listener = listener;
  }

  public interface CommentOnClickListener {

    void commentOnClick(String commentId);

    void replyOnClick(InteractionCommentReplyList data);

    void replyOnClick2(InteractionCommentReplyList data);

    void childContentOnClick(InteractionCommentReplyList data);
  }

  private class CommentImageSpan extends ImageSpan {

    private WeakReference<Drawable> mDrawableRef;

    CommentImageSpan() {
      super(getContext(), OWNER_DRAWABLE_RES_ID);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
        int top, int y, int bottom, @NonNull Paint paint) {
      // image to draw
      Drawable b = getCachedDrawable();
      // font metrics of text to be replaced
      Paint.FontMetricsInt fm = paint.getFontMetricsInt();
      int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;

      canvas.save();
      canvas.translate(x, transY);
      b.draw(canvas);
      canvas.restore();
    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private Drawable getCachedDrawable() {
      WeakReference<Drawable> wr = mDrawableRef;
      Drawable d = null;

      if (wr != null) {
        d = wr.get();
      }

      if (d == null) {
        d = getDrawable();
        mDrawableRef = new WeakReference<>(d);
      }

      return d;
    }
  }

  private class CommentClickableSpan extends ClickableSpan {

    CommentOnClickListener listener;
    int type;
    int color = R.color.interaction_text_font;

    CommentClickableSpan(int clickType, CommentOnClickListener listener) {
      this.listener = listener;
      type = clickType;
    }

    CommentClickableSpan(int clickType, CommentOnClickListener listener, int color) {
      this.listener = listener;
      type = clickType;
      this.color = color;
    }

    @Override public void onClick(View widget) {
      if (listener == null) {
        return;
      }
      switch (type) {
        case COMMENT_CLICK:
          listener.replyOnClick(getCommentData());
          break;
        case REPORT_CLICK:
          listener.replyOnClick(getCommentData());
          break;
        case CONTENT_CLICK:
          listener.childContentOnClick(getCommentData());
          break;
        case REPORT_CLICK2:
          listener.replyOnClick2(getCommentData());
          break;
      }
    }

    @Override public void updateDrawState(TextPaint ds) {
      super.updateDrawState(ds);
      ds.setColor(ContextCompat.getColor(getContext(), color));
      ds.setUnderlineText(false);
      ds.bgColor = Color.TRANSPARENT;
    }
  }
}
