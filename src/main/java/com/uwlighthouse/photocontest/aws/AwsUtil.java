package com.uwlighthouse.photocontest.aws;

import java.io.IOException;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AwsUtil {
	public static AmazonS3 getS3Client() throws IOException {
		return new AmazonS3Client(new PropertiesCredentials(AwsUtil.class.getResourceAsStream("AwsCredentials.properties")));
	}
}
