/*
 * Copyright (C) 2017-2017 DataStax Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.cassandra.protocol.internal.response.result;

import com.datastax.cassandra.protocol.internal.Message;
import com.datastax.cassandra.protocol.internal.PrimitiveCodec;
import com.datastax.cassandra.protocol.internal.ProtocolConstants;
import com.datastax.cassandra.protocol.internal.response.Result;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Rows extends Result {
  public final RowsMetadata metadata;
  public final Queue<List<ByteBuffer>> data;

  public Rows(RowsMetadata metadata, Queue<List<ByteBuffer>> data) {
    super(ProtocolConstants.ResponseKind.ROWS);
    this.metadata = metadata;
    this.data = data;
  }

  public static class SubCodec extends Result.SubCodec {
    public SubCodec(int protocolVersion) {
      super(ProtocolConstants.ResponseKind.ROWS, protocolVersion);
    }

    @Override
    public <B> void encode(B dest, Message message, PrimitiveCodec<B> encoder) {
      throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int encodedSize(Message message) {
      throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <B> Message decode(B source, PrimitiveCodec<B> decoder) {
      RowsMetadata metadata = RowsMetadata.decodeWithoutPkIndices(source, decoder, protocolVersion);
      int rowCount = decoder.readInt(source);
      int columnCount = metadata.columnCount;

      Queue<List<ByteBuffer>> data = new ArrayDeque<>(rowCount);
      for (int i = 0; i < rowCount; i++) {
        List<ByteBuffer> row = new ArrayList<>(columnCount);
        for (int j = 0; j < columnCount; j++) row.add(decoder.readBytes(source));
        data.add(row);
      }

      return new Rows(metadata, data);
    }
  }
}