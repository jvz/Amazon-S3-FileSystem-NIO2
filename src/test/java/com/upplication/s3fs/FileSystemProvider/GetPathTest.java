package com.upplication.s3fs.FileSystemProvider;

import com.google.common.collect.ImmutableMap;
import com.upplication.s3fs.S3FileSystemProvider;
import com.upplication.s3fs.S3UnitTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GetPathTest extends S3UnitTestBase {

    private S3FileSystemProvider s3fsProvider;

    @Before
    public void setup() {
        s3fsProvider = spy(new S3FileSystemProvider());
        doReturn(false).when(s3fsProvider).overloadPropertiesWithSystemEnv(any(Properties.class), anyString());
        doReturn(new Properties()).when(s3fsProvider).loadAmazonProperties();
    }


    @Test
    public void getPathWithEmtpyEndpoint() throws IOException {
        FileSystem fs = FileSystems.newFileSystem(URI.create("s3:///"), ImmutableMap.<String, Object>of());
        Path path = fs.provider().getPath(URI.create("s3:///bucket/path/to/file"));

        assertEquals(path, fs.getPath("/bucket/path/to/file"));
        assertSame(path.getFileSystem(), fs);
    }

    @Test
    public void getPath() throws IOException {

        FileSystem fs = FileSystems.newFileSystem(URI.create("s3://endpoint1/"), null);
        Path path = fs.provider().getPath(URI.create("s3://endpoint1/bucket/path/to/file"));

        assertEquals(path, fs.getPath("/bucket/path/to/file"));
        assertSame(path.getFileSystem(), fs);
    }

    @Test
    public void getAnotherPath() throws IOException {
        FileSystem fs = FileSystems.newFileSystem(URI.create("s3://endpoint1/"), ImmutableMap.<String, Object>of());
        Path path = fs.provider().getPath(URI.create("s3://endpoint1/bucket/path/to/file"));

        assertEquals(path, fs.getPath("/bucket/path/to/file"));
        assertSame(path.getFileSystem(), fs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathWithEndpointAndWithoutBucket() throws IOException {
        FileSystem fs = FileSystems.newFileSystem(URI.create("s3://endpoint1/"), null);
        fs.provider().getPath(URI.create("s3://endpoint1//falta-bucket"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathWithDefaultEndpointAndWithoutBucket() throws IOException {
        FileSystem fs = FileSystems.newFileSystem(URI.create("s3:///"), ImmutableMap.<String, Object>of());
        fs.provider().getPath(URI.create("s3:////falta-bucket"));
    }
}