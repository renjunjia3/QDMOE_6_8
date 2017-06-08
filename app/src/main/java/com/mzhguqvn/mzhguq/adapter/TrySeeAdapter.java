package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.TrySeeContentInfo;
import com.mzhguqvn.mzhguq.util.GlideUtils;
import com.mzhguqvn.mzhguq.util.ScreenUtils;
import com.mzhguqvn.mzhguq.util.ViewUtils;
import com.mzhguqvn.mzhguq.video.JCUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:试看数据
 * package:com.hfaufhreu.hjfeuio.adapter
 * Author：scene on 2017/4/18 20:53
 */

public class TrySeeAdapter extends BaseAdapter {
    private static final int TYPE_BIG = 0;
    private static final int TYPE_HORIZONTAL = 1;
    private static final int TYPE_VERTICAL = 2;

    private Context context;
    private List<TrySeeContentInfo> lists;

    private LayoutInflater inflater;
    private static ScreenUtils screenUtils;

    private OnItemClickListener onItemClickListener;

    public TrySeeAdapter(Context context, List<TrySeeContentInfo> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        screenUtils = ScreenUtils.instance(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1 = null;
        ViewHolder2 viewHolder2 = null;
        ViewHolder3 viewHolder3 = null;
        if (convertView == null) {
            if (getItemViewType(position) == TYPE_BIG) {
                convertView = inflater.inflate(R.layout.fragment_try_see_item3, null);
                viewHolder3 = new ViewHolder3(convertView);
                convertView.setTag(viewHolder3);
            } else if (getItemViewType(position) == TYPE_VERTICAL) {
                convertView = inflater.inflate(R.layout.fragment_try_see_item1, null);
                viewHolder1 = new ViewHolder1(convertView);
                convertView.setTag(viewHolder1);
            } else {
                convertView = inflater.inflate(R.layout.fragment_try_see_item2, null);
                viewHolder2 = new ViewHolder2(convertView);
                convertView.setTag(viewHolder2);
            }
        } else {
            if (getItemViewType(position) == TYPE_BIG) {
                viewHolder3 = (ViewHolder3) convertView.getTag();
            } else if (getItemViewType(position) == TYPE_VERTICAL) {
                viewHolder1 = (ViewHolder1) convertView.getTag();
            } else {
                viewHolder2 = (ViewHolder2) convertView.getTag();
            }
        }
        TrySeeContentInfo info = lists.get(position);
        if (getItemViewType(position) == TYPE_BIG) {
            viewHolder3.name.setText(info.getTitle());
            if (info.getData() != null && info.getData().size() > 0) {
                viewHolder3.recommendName.setText(info.getData().get(0).getTitle());
                viewHolder3.recommendSynop.setText(info.getData().get(0).getDescription());
                GlideUtils.loadImage(context, viewHolder3.recommendImage, info.getData().get(0).getThumb());
                viewHolder3.time.setText(JCUtils.stringForTime(info.getData().get(0).getDuration()));
                viewHolder3.recommendLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 0);
                        }
                    }
                });
            }
        } else if (getItemViewType(position) == TYPE_VERTICAL) {
            viewHolder1.name.setText(info.getTitle());
            try {
                viewHolder1.vipName1.setText(info.getData().get(0).getTitle());
                viewHolder1.vipPlayTime1.setText(info.getData().get(0).getHits() + "次访问");
                GlideUtils.loadImage(context, viewHolder1.vipImage1, info.getData().get(0).getThumb());
                viewHolder1.time1.setText(JCUtils.stringForTime(info.getData().get(0).getDuration()));
                if (info.getData().get(0).getTag() != null && !info.getData().get(0).getTag().isEmpty()) {
                    viewHolder1.vipTag1.setText(info.getData().get(0).getTag());
                    viewHolder1.vipTag1.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag1.setVisibility(View.GONE);
                }
                if (info.getData().get(0).getTag_color() != null && !info.getData().get(0).getTag_color().isEmpty()) {
                    viewHolder1.vipTag1.setBackgroundColor(Color.parseColor(info.getData().get(0).getTag_color()));
                } else {
                    viewHolder1.vipTag1.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                viewHolder1.vipLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 0);
                        }
                    }
                });

                viewHolder1.vipName2.setText(info.getData().get(1).getTitle());
                viewHolder1.vipPlayTime2.setText(info.getData().get(1).getHits() + "次访问");
                viewHolder1.time2.setText(JCUtils.stringForTime(info.getData().get(2).getDuration()));
                if (info.getData().get(1).getTag() != null && !info.getData().get(1).getTag().isEmpty()) {
                    viewHolder1.vipTag2.setText(info.getData().get(1).getTag());
                    viewHolder1.vipTag2.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag2.setVisibility(View.GONE);
                }
                if (info.getData().get(1).getTag_color() != null && !info.getData().get(1).getTag_color().isEmpty()) {
                    viewHolder1.vipTag2.setBackgroundColor(Color.parseColor(info.getData().get(1).getTag_color()));
                } else {
                    viewHolder1.vipTag2.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                GlideUtils.loadImage(context, viewHolder1.vipImage2, info.getData().get(1).getThumb());
                viewHolder1.vipLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 1);
                        }
                    }
                });

                viewHolder1.vipName3.setText(info.getData().get(2).getTitle());
                viewHolder1.vipPlayTime3.setText(info.getData().get(2).getHits() + "次访问");
                viewHolder1.time3.setText(JCUtils.stringForTime(info.getData().get(2).getDuration()));
                if (info.getData().get(2).getTag() != null && !info.getData().get(2).getTag().isEmpty()) {
                    viewHolder1.vipTag3.setText(info.getData().get(2).getTag());
                    viewHolder1.vipTag3.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag3.setVisibility(View.GONE);
                }
                if (info.getData().get(2).getTag_color() != null && !info.getData().get(2).getTag_color().isEmpty()) {
                    viewHolder1.vipTag3.setBackgroundColor(Color.parseColor(info.getData().get(2).getTag_color()));
                } else {
                    viewHolder1.vipTag3.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                GlideUtils.loadImage(context, viewHolder1.vipImage3, info.getData().get(2).getThumb());
                viewHolder1.vipLayout3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 2);
                        }
                    }
                });

                viewHolder1.vipName4.setText(info.getData().get(3).getTitle());
                viewHolder1.vipPlayTime4.setText(info.getData().get(3).getHits() + "次访问");
                viewHolder1.time4.setText(JCUtils.stringForTime(info.getData().get(3).getDuration()));
                if (info.getData().get(3).getTag() != null && !info.getData().get(3).getTag().isEmpty()) {
                    viewHolder1.vipTag4.setText(info.getData().get(3).getTag());
                    viewHolder1.vipTag4.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag4.setVisibility(View.GONE);
                }
                if (info.getData().get(3).getTag_color() != null && !info.getData().get(3).getTag_color().isEmpty()) {
                    viewHolder1.vipTag4.setBackgroundColor(Color.parseColor(info.getData().get(3).getTag_color()));
                } else {
                    viewHolder1.vipTag4.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                GlideUtils.loadImage(context, viewHolder1.vipImage4, info.getData().get(3).getThumb());
                viewHolder1.vipLayout4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 3);
                        }
                    }
                });

                viewHolder1.vipName5.setText(info.getData().get(4).getTitle());
                viewHolder1.vipPlayTime5.setText(info.getData().get(4).getHits() + "次访问");
                viewHolder1.time5.setText(JCUtils.stringForTime(info.getData().get(5).getDuration()));
                if (info.getData().get(4).getTag() != null && !info.getData().get(4).getTag().isEmpty()) {
                    viewHolder1.vipTag5.setText(info.getData().get(4).getTag());
                    viewHolder1.vipTag5.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag5.setVisibility(View.GONE);
                }
                if (info.getData().get(4).getTag_color() != null && !info.getData().get(4).getTag_color().isEmpty()) {
                    viewHolder1.vipTag5.setBackgroundColor(Color.parseColor(info.getData().get(4).getTag_color()));
                } else {
                    viewHolder1.vipTag5.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                GlideUtils.loadImage(context, viewHolder1.vipImage5, info.getData().get(4).getThumb());
                viewHolder1.vipLayout5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 4);
                        }
                    }
                });
                viewHolder1.vipName6.setText(info.getData().get(5).getTitle());
                viewHolder1.vipPlayTime6.setText(info.getData().get(5).getHits() + "次访问");
                viewHolder1.time6.setText(JCUtils.stringForTime(info.getData().get(5).getDuration()));
                if (info.getData().get(5).getTag() != null && !info.getData().get(5).getTag().isEmpty()) {
                    viewHolder1.vipTag6.setText(info.getData().get(5).getTag());
                    viewHolder1.vipTag6.setVisibility(View.VISIBLE);
                } else {
                    viewHolder1.vipTag6.setVisibility(View.GONE);
                }
                if (info.getData().get(5).getTag_color() != null && !info.getData().get(5).getTag_color().isEmpty()) {
                    viewHolder1.vipTag6.setBackgroundColor(Color.parseColor(info.getData().get(5).getTag_color()));
                } else {
                    viewHolder1.vipTag6.setBackgroundColor(Color.parseColor("#FF33CC"));
                }
                GlideUtils.loadImage(context, viewHolder1.vipImage6, info.getData().get(5).getThumb());
                viewHolder1.vipLayout6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 5);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            viewHolder2.name.setText(info.getTitle());
            try {
                viewHolder2.hotName1.setText(info.getData().get(0).getTitle());
                viewHolder2.hotPlayTime1.setText(info.getData().get(0).getHits() + "次访问");
                viewHolder2.time1.setText(JCUtils.stringForTime(info.getData().get(0).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage1, info.getData().get(0).getThumb());
                if (info.getData().get(0).getTag() != null && !info.getData().get(0).getTag().isEmpty()) {
                    viewHolder2.tag1.setText(info.getData().get(0).getTag());
                    viewHolder2.tag1.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag1.setVisibility(View.GONE);
                }

                if (info.getData().get(0).getTag_color() != null && !info.getData().get(0).getTag_color().isEmpty()) {
                    viewHolder2.tag1.setBackgroundColor(Color.parseColor(info.getData().get(0).getTag_color()));
                } else {
                    viewHolder2.tag1.setBackgroundColor(Color.parseColor("FF33CC"));
                }

                viewHolder2.hotLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 0);
                        }
                    }
                });

                viewHolder2.hotName2.setText(info.getData().get(1).getTitle());
                viewHolder2.hotPlayTime2.setText(info.getData().get(1).getHits() + "次访问");
                viewHolder2.time2.setText(JCUtils.stringForTime(info.getData().get(1).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage2, info.getData().get(1).getThumb());
                if (info.getData().get(1).getTag() != null && !info.getData().get(1).getTag().isEmpty()) {
                    viewHolder2.tag2.setText(info.getData().get(1).getTag());
                    viewHolder2.tag2.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag2.setVisibility(View.GONE);
                }
                if (info.getData().get(1).getTag_color() != null && !info.getData().get(1).getTag_color().isEmpty()) {
                    viewHolder2.tag2.setBackgroundColor(Color.parseColor(info.getData().get(1).getTag_color()));
                } else {
                    viewHolder2.tag2.setBackgroundColor(Color.parseColor("FF33CC"));
                }
                viewHolder2.hotLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 1);
                        }
                    }
                });

                viewHolder2.hotName3.setText(info.getData().get(2).getTitle());
                viewHolder2.hotPlayTime3.setText(info.getData().get(2).getHits() + "次访问");
                viewHolder2.time3.setText(JCUtils.stringForTime(info.getData().get(2).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage3, info.getData().get(2).getThumb());
                if (info.getData().get(2).getTag() != null && !info.getData().get(2).getTag().isEmpty()) {
                    viewHolder2.tag3.setText(info.getData().get(2).getTag());
                    viewHolder2.tag3.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag3.setVisibility(View.GONE);
                }
                if (info.getData().get(2).getTag_color() != null && !info.getData().get(2).getTag_color().isEmpty()) {
                    viewHolder2.tag3.setBackgroundColor(Color.parseColor(info.getData().get(2).getTag_color()));
                } else {
                    viewHolder2.tag3.setBackgroundColor(Color.parseColor("FF33CC"));
                }
                viewHolder2.hotLayout3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 2);
                        }
                    }
                });


                viewHolder2.hotName4.setText(info.getData().get(3).getTitle());
                viewHolder2.hotPlayTime4.setText(info.getData().get(3).getHits() + "次访问");
                viewHolder2.time4.setText(JCUtils.stringForTime(info.getData().get(3).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage4, info.getData().get(3).getThumb());
                if (info.getData().get(3).getTag() != null && !info.getData().get(3).getTag().isEmpty()) {
                    viewHolder2.tag4.setText(info.getData().get(3).getTag());
                    viewHolder2.tag4.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag4.setVisibility(View.GONE);
                }
                if (info.getData().get(3).getTag_color() != null && !info.getData().get(3).getTag_color().isEmpty()) {
                    viewHolder2.tag4.setBackgroundColor(Color.parseColor(info.getData().get(3).getTag_color()));
                } else {
                    viewHolder2.tag4.setBackgroundColor(Color.parseColor("FF33CC"));
                }
                viewHolder2.hotLayout4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 3);
                        }
                    }
                });


                viewHolder2.hotName5.setText(info.getData().get(4).getTitle());
                viewHolder2.hotPlayTime5.setText(info.getData().get(4).getHits() + "次访问");
                viewHolder2.time5.setText(JCUtils.stringForTime(info.getData().get(4).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage5, info.getData().get(4).getThumb());
                if (info.getData().get(4).getTag() != null && !info.getData().get(4).getTag().isEmpty()) {
                    viewHolder2.tag5.setText(info.getData().get(4).getTag());
                    viewHolder2.tag5.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag5.setVisibility(View.GONE);
                }
                if (info.getData().get(4).getTag_color() != null && !info.getData().get(4).getTag_color().isEmpty()) {
                    viewHolder2.tag5.setBackgroundColor(Color.parseColor(info.getData().get(4).getTag_color()));
                } else {
                    viewHolder2.tag5.setBackgroundColor(Color.parseColor("FF33CC"));
                }
                viewHolder2.hotLayout5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 4);
                        }
                    }
                });

                viewHolder2.hotName6.setText(info.getData().get(5).getTitle());
                viewHolder2.hotPlayTime6.setText(info.getData().get(5).getHits() + "次访问");
                viewHolder2.time6.setText(JCUtils.stringForTime(info.getData().get(5).getDuration()));
                GlideUtils.loadImage(context, viewHolder2.hotImage6, info.getData().get(5).getThumb());
                if (info.getData().get(5).getTag() != null && !info.getData().get(5).getTag().isEmpty()) {
                    viewHolder2.tag6.setText(info.getData().get(5).getTag());
                    viewHolder2.tag6.setVisibility(View.VISIBLE);
                } else {
                    viewHolder2.tag6.setVisibility(View.GONE);
                }
                if (info.getData().get(5).getTag_color() != null && !info.getData().get(5).getTag_color().isEmpty()) {
                    viewHolder2.tag6.setBackgroundColor(Color.parseColor(info.getData().get(5).getTag_color()));
                } else {
                    viewHolder2.tag6.setBackgroundColor(Color.parseColor("FF33CC"));
                }
                viewHolder2.hotLayout6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onTrySeeItemClick(position, 5);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        int type = lists.get(position).getType();
        if (type == 2) {
            return TYPE_BIG;
        } else if (type == 3) {
            return TYPE_VERTICAL;
        } else {
            return TYPE_HORIZONTAL;
        }

    }


    /**
     * Case By: 大图的item
     * package:
     * Author：scene on 2017/4/18 20:56
     */
    static class ViewHolder3 {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.recommend_image)
        ImageView recommendImage;
        @BindView(R.id.recommend_name)
        TextView recommendName;
        @BindView(R.id.recommend_synop)
        TextView recommendSynop;
        @BindView(R.id.recommend_layout)
        LinearLayout recommendLayout;
        @BindView(R.id.time)
        TextView time;

        ViewHolder3(View view) {
            ButterKnife.bind(this, view);
            ViewUtils.setViewHeightByViewGroup(recommendImage, (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(20)) * 9f / 16f));
        }
    }

    /**
     * Case By: 每行3个的item
     * package:
     * Author：scene on 2017/4/18 20:56
     */
    static class ViewHolder1 {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.vip_more)
        TextView vipMore;
        @BindView(R.id.vip_imageTop)
        ImageView vipImageTop;
        @BindView(R.id.vip_nameTop)
        TextView vipNameTop;
        @BindView(R.id.vip_layoutTop)
        RelativeLayout vipLayoutTop;
        @BindView(R.id.vip_image1)
        ImageView vipImage1;
        @BindView(R.id.vip_tag1)
        TextView vipTag1;
        @BindView(R.id.vip_name1)
        TextView vipName1;
        @BindView(R.id.vip_play_time1)
        TextView vipPlayTime1;
        @BindView(R.id.vip_layout1)
        LinearLayout vipLayout1;
        @BindView(R.id.vip_image2)
        ImageView vipImage2;
        @BindView(R.id.vip_tag2)
        TextView vipTag2;
        @BindView(R.id.vip_name2)
        TextView vipName2;
        @BindView(R.id.vip_play_time2)
        TextView vipPlayTime2;
        @BindView(R.id.vip_layout2)
        LinearLayout vipLayout2;
        @BindView(R.id.vip_image3)
        ImageView vipImage3;
        @BindView(R.id.vip_tag3)
        TextView vipTag3;
        @BindView(R.id.vip_name3)
        TextView vipName3;
        @BindView(R.id.vip_play_time3)
        TextView vipPlayTime3;
        @BindView(R.id.vip_layout3)
        LinearLayout vipLayout3;
        @BindView(R.id.vip_image4)
        ImageView vipImage4;
        @BindView(R.id.vip_tag4)
        TextView vipTag4;
        @BindView(R.id.vip_name4)
        TextView vipName4;
        @BindView(R.id.vip_play_time4)
        TextView vipPlayTime4;
        @BindView(R.id.vip_layout4)
        LinearLayout vipLayout4;
        @BindView(R.id.vip_image5)
        ImageView vipImage5;
        @BindView(R.id.vip_tag5)
        TextView vipTag5;
        @BindView(R.id.vip_name5)
        TextView vipName5;
        @BindView(R.id.vip_play_time5)
        TextView vipPlayTime5;
        @BindView(R.id.vip_layout5)
        LinearLayout vipLayout5;
        @BindView(R.id.vip_image6)
        ImageView vipImage6;
        @BindView(R.id.vip_tag6)
        TextView vipTag6;
        @BindView(R.id.vip_name6)
        TextView vipName6;
        @BindView(R.id.vip_play_time6)
        TextView vipPlayTime6;
        @BindView(R.id.vip_layout6)
        LinearLayout vipLayout6;
        @BindView(R.id.vip_layout)
        LinearLayout vipLayout;
        @BindView(R.id.time1)
        TextView time1;
        @BindView(R.id.time2)
        TextView time2;
        @BindView(R.id.time3)
        TextView time3;
        @BindView(R.id.time4)
        TextView time4;
        @BindView(R.id.time5)
        TextView time5;
        @BindView(R.id.time6)
        TextView time6;


        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
            int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(12)) * 24 / 3f / 18f);
            ViewUtils.setViewHeightByViewGroup(vipImage1, height);
            ViewUtils.setViewHeightByViewGroup(vipImage2, height);
            ViewUtils.setViewHeightByViewGroup(vipImage3, height);
            ViewUtils.setViewHeightByViewGroup(vipImage4, height);
            ViewUtils.setViewHeightByViewGroup(vipImage5, height);
            ViewUtils.setViewHeightByViewGroup(vipImage6, height);
        }
    }

    /**
     * Case By: 每行2个的item
     * package:
     * Author：scene on 2017/4/18 20:56
     */
    static class ViewHolder2 {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.hot_more)
        TextView hotMore;
        @BindView(R.id.hot_image1)
        ImageView hotImage1;
        @BindView(R.id.hot_name1)
        TextView hotName1;
        @BindView(R.id.hot_play_time1)
        TextView hotPlayTime1;
        @BindView(R.id.hot_layout1)
        LinearLayout hotLayout1;
        @BindView(R.id.hot_image2)
        ImageView hotImage2;
        @BindView(R.id.hot_name2)
        TextView hotName2;
        @BindView(R.id.hot_play_time2)
        TextView hotPlayTime2;
        @BindView(R.id.hot_layout2)
        LinearLayout hotLayout2;
        @BindView(R.id.hot_image3)
        ImageView hotImage3;
        @BindView(R.id.hot_name3)
        TextView hotName3;
        @BindView(R.id.hot_play_time3)
        TextView hotPlayTime3;
        @BindView(R.id.hot_layout3)
        LinearLayout hotLayout3;
        @BindView(R.id.hot_image4)
        ImageView hotImage4;
        @BindView(R.id.hot_name4)
        TextView hotName4;
        @BindView(R.id.hot_play_time4)
        TextView hotPlayTime4;
        @BindView(R.id.hot_layout4)
        LinearLayout hotLayout4;
        @BindView(R.id.hot_image5)
        ImageView hotImage5;
        @BindView(R.id.hot_name5)
        TextView hotName5;
        @BindView(R.id.hot_play_time5)
        TextView hotPlayTime5;
        @BindView(R.id.hot_layout5)
        LinearLayout hotLayout5;
        @BindView(R.id.hot_image6)
        ImageView hotImage6;
        @BindView(R.id.hot_name6)
        TextView hotName6;
        @BindView(R.id.hot_play_time6)
        TextView hotPlayTime6;
        @BindView(R.id.hot_layout6)
        LinearLayout hotLayout6;
        @BindView(R.id.hot_layout)
        LinearLayout hotLayout;
        @BindView(R.id.tag1)
        TextView tag1;
        @BindView(R.id.tag2)
        TextView tag2;
        @BindView(R.id.tag3)
        TextView tag3;
        @BindView(R.id.tag4)
        TextView tag4;
        @BindView(R.id.tag5)
        TextView tag5;
        @BindView(R.id.tag6)
        TextView tag6;
        @BindView(R.id.time1)
        TextView time1;
        @BindView(R.id.time2)
        TextView time2;
        @BindView(R.id.time3)
        TextView time3;
        @BindView(R.id.time4)
        TextView time4;
        @BindView(R.id.time5)
        TextView time5;
        @BindView(R.id.time6)
        TextView time6;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
            int height = (int) ((screenUtils.getScreenWidth() - screenUtils.dip2px(9)) * 9f / 2f / 16f);
            ViewUtils.setViewHeightByViewGroup(hotImage1, height);
            ViewUtils.setViewHeightByViewGroup(hotImage2, height);
            ViewUtils.setViewHeightByViewGroup(hotImage3, height);
            ViewUtils.setViewHeightByViewGroup(hotImage4, height);
            ViewUtils.setViewHeightByViewGroup(hotImage5, height);
            ViewUtils.setViewHeightByViewGroup(hotImage6, height);
        }
    }

    public interface OnItemClickListener {
        void onTrySeeItemClick(int position, int childPosition);
    }

}
