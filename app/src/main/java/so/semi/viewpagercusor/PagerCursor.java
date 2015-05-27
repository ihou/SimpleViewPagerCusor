package so.semi.viewpagercusor;

/**
 * @author houwenchang
 * <p/>
 * 2015/5/26.
 */


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class PagerCursor extends LinearLayout {

    private ViewPager mPager;

    private View mCursorLine;

    private int mCurrPosition = 0;

    public PagerCursor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerCursor(Context context) {
        super(context);
    }

    private int mTabWidth;

    public void config(final View[] titleView, int cursorColor) {
        mTabWidth = getContext().getResources().getDisplayMetrics().widthPixels / (titleView.length);
        if (mCurrPosition < 0) {
            mCurrPosition = 0;
        }

        FrameLayout frame = new FrameLayout(getContext());
        frame.setBackgroundColor(Color.GRAY);

        LinearLayout tabContainer = new LinearLayout(getContext());
        tabContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        tabContainer.setGravity(Gravity.CENTER);

        mCursorLine = new View(getContext());


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.width = mTabWidth;
        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                .getDisplayMetrics());
        mCursorLine.setLayoutParams(params);
//        int inset = (selectorWidth - params.width / 2) / 2;  //half

        int inset = 50;
        InsetDrawable drawable = new InsetDrawable(new ColorDrawable(cursorColor), inset, 0, inset, 0);
        mCursorLine.setBackgroundDrawable(drawable);

        for (int i = 0; i < titleView.length; i++) {
            titleView[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
            tabContainer.addView(titleView[i]);
            titleView[i].setTag(i);
            titleView[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPager.setCurrentItem((Integer) view.getTag());
                }
            });
        }

        frame.addView(tabContainer);
        frame.addView(mCursorLine);
        mCursorLine.clearAnimation();
        addView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

    }

    public void bind(ViewPager pager, final ViewPager.OnPageChangeListener listener) {
        mPager = pager;
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset != 0) {
                    mCursorLine.offsetLeftAndRight(positionOffsetPixels / 3 - mCursorLine.getLeft() + position * mTabWidth);
                }
                if (listener != null) {
                    listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mCursorLine.offsetLeftAndRight(mPager.getCurrentItem() * mTabWidth);
    }
}
