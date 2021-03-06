package com.faforever.client.remote.io;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class QDataInputStream extends InputStream {

  private final DataInput dataInput;
  private final Charset charset;

  public QDataInputStream(DataInput dataInput) {
    this(dataInput, StandardCharsets.UTF_16BE);
  }

  public QDataInputStream(DataInput dataInput, Charset charset) {
    this.dataInput = dataInput;
    this.charset = charset;
  }

  public String readQString() throws IOException {
    int stringSize = dataInput.readInt();
    if (stringSize == -1) {
      return null;
    }

    byte[] buffer = new byte[stringSize];
    dataInput.readFully(buffer);
    return new String(buffer, charset);
  }

  @Override
  public int read() throws IOException {
    return dataInput.readUnsignedByte();
  }

  @Override
  public void close() throws IOException {
    if (dataInput instanceof Closeable) {
      ((Closeable) dataInput).close();
    }
  }

  public int readInt() throws IOException {
    return dataInput.readInt();
  }

  /**
   * Skip the "block size" bytes, since we just don't care.
   */
  public void skipBlockSize() throws IOException {
    dataInput.skipBytes(Integer.BYTES);
  }
}
