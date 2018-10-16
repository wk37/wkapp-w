package aacom.wangke.wkappw.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseGuideBanner;

import aacom.wangke.wkappw.R;


//import com.squareup.picasso.Picasso;

/**
 * ******************(^_^)***********************
 * User: 11776610771@qq.com
 * Date: 2017/4/6
 * Time: 14:41
 * DESC: 这里提供一个默认布局的导航Banner
 * ******************(^_^)***********************
 */

public class SimpleGuideBanner extends BaseGuideBanner<Integer> {
    public SimpleGuideBanner(Context context) {
        this(context, null);
    }

    ImageView imgKey1;
    //是否已经关闭了banner
    private boolean isCloseBanner = false;

    public SimpleGuideBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBarShowWhenLast(true);//在滑动到最后一页时不显示指示器布局
        setIndicatorHeight(15);
        setIndicatorWidth(15);
        setIndicatorStyle(0);
        setIndicatorSelectorRes(R.drawable.welcome_dot1, R.drawable.welcome_dot2);
        setIndicatorGap(15);
        barPadding(0, 0, 0, 30);


    }

    //是否关闭了欢迎页
    public void setCloseBanner(boolean isCloseBanner) {
        this.isCloseBanner = isCloseBanner;
    }

    /**
     * 根据提供的数据集来获取每一页的视图
     *
     * @param itemData
     * @param itemPosition
     * @return
     */
    @Override
    protected View getItemViewBaseData(Integer itemData, int itemPosition) {
        return View.inflate(mContext, R.layout.simple_guide_banner_layout, null);
    }

    /**
     * 初始化当前的导航页的视图，这个要交给具体子类去实现，因为本基类不知道，滑动导航到各页时，需要初始化哪些子View，并且不知道要做什么
     *
     * @param curView         当前的导航页(视图)
     * @param curViewPosition 当前导航页(视图)的位置
     */
    @Override
    public void initGuideView(View curView, int curViewPosition) {
        TextView tvJumpBtn = (TextView) curView.findViewById(R.id.tv_jump_over);
        ImageView ivGuidePic = (ImageView) curView.findViewById(R.id.iv_guide_pic);

        int guidePicResId = getItemData(curViewPosition);
        if (curViewPosition != 0) {
            Glide.with(mContext).load(guidePicResId).into(ivGuidePic);
        }

        tvJumpBtn.setVisibility(GONE);
        if (curViewPosition == (getDataCountInBanner() - 1)) {
            tvJumpBtn.setVisibility(VISIBLE);
        }
        tvJumpBtn.setOnClickListener(outSideClickListener);

    }

    private OnClickListener outSideClickListener;

    public void setOutSideOnClickListener(OnClickListener l) {
        this.outSideClickListener = l;
    }





}
