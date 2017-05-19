package com.example.ryosuke.speaklabstory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ryosuke on 17/05/13.
 */

public interface FileIO {

    public InputStream readAsset(String fileName) throws IOException;

    public InputStream readFile(String fileName) throws IOException;

    public OutputStream writeFile(String fileName) throws IOException;

}
