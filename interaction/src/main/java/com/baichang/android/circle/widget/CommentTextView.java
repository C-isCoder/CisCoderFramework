package com.baichang.android.circle.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import com.baichang.android.circle.R;
import com.baichang.android.circle.entity.InteractionCommentListData;
import com.baichang.android.utils.BCDensityUtil;
import java.lang.ref.WeakReference;

/**
 * Created by iCong on 2017/3/22.
 */

public class CommentTextView extends android.support.v7.widget.AppCompatTextView {

  private static final float mTextSize = 13;
  private static final float mPadding = 4f;
  private static final String REPORT_FLAG_TEXT = " 回复 ";
  private static final int OWNER_CLICK = 1000;
  private static final int REPORT_CLICK = 1001;
  private static final int CONTENT_CLICK = 1002;
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
    setTextColor(getColor(R.color.cm_tv_black1));
    setLineSpacing(4, 1);
    setPadding(0, getPadding(getContext()), 0, getPadding(getContext()));
    setAutoLinkMask(Linkify.PHONE_NUMBERS);
    setGravity(Gravity.CENTER_VERTICAL);
    setMovementMethod(LinkMovementMethod.getInstance());
  }

  public void setText(InteractionCommentListData comment) {
    if (comment == null) {
      return;
    }
    if (!comment.owner.contains(":")) {
      comment.owner += ":  ";
    }
    setTag(comment);
    SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
    if (TextUtils.isEmpty(comment.reportName)) {
      stringBuilder.append(comment.owner).append(comment.content);
      stringBuilder.setSpan(new CommentImageSpan(),
          comment.owner.length() - 1, comment.owner.length(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      stringBuilder.setSpan(new CommentClickableSpan(OWNER_CLICK, listener),
          0, comment.content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      stringBuilder.setSpan(new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1),
          comment.owner.length(), comment.owner.length() + comment.content.length(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    } else {
      stringBuilder.append(comment.reportName).append(REPORT_FLAG_TEXT).append(comment.owner)
          .append(comment.content);
      stringBuilder.setSpan(new CommentImageSpan(),
          comment.reportName.length() + getReportLength() + comment.owner.length() - 1,
          comment.reportName.length() + getReportLength() + comment.owner.length(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      stringBuilder.setSpan(new CommentClickableSpan(REPORT_CLICK, listener),
          0, comment.reportName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      stringBuilder.setSpan(new CommentClickableSpan(OWNER_CLICK, listener),
          comment.reportName.length() + getReportLength(),
          comment.reportName.length() + getReportLength() + comment.owner.length(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      stringBuilder.setSpan(new CommentClickableSpan(CONTENT_CLICK, listener, R.color.cm_tv_black1)
          , comment.reportName.length() + getReportLength() + comment.owner.length(),
          comment.reportName.length() + getReportLength() + comment.owner.length() + comment.content.length(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    setText(stringBuilder);
  }

  private int getPadding(Context context) {
    return BCDensityUtil.dip2px(context, mPadding);
  }

  private int getColor(int colorRes) {
    return getResources().getColor(colorRes);
  }

  private int getReportLength() {
    return REPORT_FLAG_TEXT.length();
  }

  public InteractionCommentListData getCommentData() {
    return (InteractionCommentListData) getTag();
  }

  private CommentOnClickListener listener;

  public void setCommentOnClickListener(CommentOnClickListener listener) {
    this.listener = listener;
  }

  public interface CommentOnClickListener {

    void ownerOnClick(InteractionCommentListData data);

    void reportOnClick(InteractionCommentListData data);

    void contentOnClick(InteractionCommentListData data);
  }

  private class CommentImageSpan extends ImageSpan {

    private WeakReference<Drawable> mDrawableRef;

    CommentImageSpan() {
      super(getContext(), OWNER_DRAWABLE_RES_ID);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
        int start, int end, float x,
        int top, int y, int bottom, @NonNull Paint paint) {
      // image to draw
      Drawable b = getCachedDrawable();
      // font metrics of text to be replaced
      Paint.FontMetricsInt fm = paint.getFontMetricsInt();
      int transY = (y + fm.descent + y + fm.ascent) / 2
          - b.getBounds().bottom / 2;

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

    CommentClickableSpan(int clickType,
        CommentOnClickListener listener) {
      this.listener = listener;
      type = clickType;
    }

    CommentClickableSpan(int clickType,
        CommentOnClickListener listener, int color) {
      this.listener = listener;
      type = clickType;
      this.color = color;
    }

    @Override
    public void onClick(View widget) {
      if (listener == null) {
        return;
      }
      switch (type) {
        case OWNER_CLICK:
          listener.ownerOnClick(getCommentData());
          break;
        case REPORT_CLICK:
          listener.reportOnClick(getCommentData());
          break;
        case CONTENT_CLICK:
          listener.contentOnClick(getCommentData());
          break;
      }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
      super.updateDrawState(ds);
      ds.setColor(getColor(color));
      ds.setUnderlineText(false);
      ds.bgColor = Color.TRANSPARENT;
    }
  }
}
