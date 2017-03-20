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
package com.datastax.cassandra.protocol.internal.response;

import com.datastax.cassandra.protocol.internal.Message;
import com.datastax.cassandra.protocol.internal.MessageTest;
import com.datastax.cassandra.protocol.internal.TestDataProviders;
import com.datastax.cassandra.protocol.internal.binary.MockBinaryString;
import org.testng.annotations.Test;

import static com.datastax.cassandra.protocol.internal.Assertions.assertThat;

public class ReadyTest extends MessageTest<Ready> {
  protected ReadyTest() {
    super(Ready.class);
  }

  @Override
  protected Message.Codec newCodec(int protocolVersion) {
    return new Ready.Codec(protocolVersion);
  }

  @Test(dataProviderClass = TestDataProviders.class, dataProvider = "protocolV3OrAbove")
  public void should_decode_from_empty_body(int protocolVersion) {
    assertThat(decode(new MockBinaryString(), protocolVersion)).isInstanceOf(Ready.class);
  }
}