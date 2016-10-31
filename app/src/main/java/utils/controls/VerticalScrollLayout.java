package utils.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Jonatas on 30/10/2016.
 */
public class VerticalScrollLayout extends ScrollView {

    private GestureDetector gdScrolling;
    private boolean isScrolling;

    private OnScrollListener lOnScroll;

    public VerticalScrollLayout(Context context) {
        super(context);
        init(context);
    }

    public VerticalScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private  void init(Context context) {

        setVerticalScrollBarEnabled(false);

        gdScrolling = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                isScrolling = true;
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gdScrolling.onTouchEvent(event)) {
                    return false;
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(isScrolling ) {
                        isScrolling  = false;
                        if(lOnScroll != null) {
                            lOnScroll.onScrollEnded();
                        }
                    }
                }

                return false;
            }
        });
    }

    public void setOnScrollListener(OnScrollListener l) {
        lOnScroll = l;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(lOnScroll != null) {
            lOnScroll.onScrollChanged(x, y, oldx, oldy);
        }
    }

    public interface OnScrollListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);
        void onScrollEnded();
    }
}