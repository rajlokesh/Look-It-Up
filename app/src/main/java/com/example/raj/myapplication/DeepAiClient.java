package com.example.raj.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeepAiClient {
    @FormUrlEncoded
    @POST("api/densecap")
    @Headers("api-key: aa045484-2e4b-4517-9256-81d2dcf6a700")
    Call<CaptionItem> captionForUrl(
            @Field("image") String url);

//    @Multipart
//    @POST("api/densecap")
//    @Headers("api-key: aa045484-2e4b-4517-9256-81d2dcf6a700")
//    Call<CaptionItem> uploadPhoto(
//            @Part("image") MultipartBody.Part image
//    );
}


