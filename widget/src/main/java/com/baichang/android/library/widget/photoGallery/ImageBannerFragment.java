package com.baichang.android.library.widget.photoGallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baichang.android.library.imageloader.ImageLoader;
import com.baichang.android.library.widget.R;
import com.baichang.android.library.widget.photoView.PhotoView;

public class ImageBannerFragment extends Fragment {
    private PhotoView mPhoto;
    private static final String ARG_PARAM = "param1";

    private String imageUrl;


    public ImageBannerFragment() {
    }

    public static ImageBannerFragment newInstance(String param) {
        ImageBannerFragment fragment = new ImageBannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery_image_banner, container, false);
        mPhoto = (PhotoView) view.findViewById(R.id.fragment_photo_gallery_image_banner_image);
        initView();
        return view;
    }

    private void initView() {
        ImageLoader.loadImage(getActivity().getApplicationContext(), imageUrl, mPhoto);
    }

}
