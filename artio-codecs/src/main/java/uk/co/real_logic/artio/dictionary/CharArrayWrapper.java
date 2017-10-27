/*
 * Copyright 2015-2017 Real Logic Ltd.
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
package uk.co.real_logic.artio.dictionary;

import uk.co.real_logic.artio.dictionary.generation.CodecUtil;

final class CharArrayWrapper
{
    private char[] values;
    private int length;
    private int hashcode;

    CharArrayWrapper()
    {
    }

    CharArrayWrapper(final CharArrayWrapper other)
    {
        this.values = other.values;
        this.length = other.length;
        this.hashcode = other.hashcode;
    }

    CharArrayWrapper(final String string)
    {
        final char[] values = string.toCharArray();
        wrap(values, values.length);
    }

    void wrap(final char[] values, final int length)
    {
        this.values = values;
        this.length = length;
        hashcode = CodecUtil.hashCode(values, length);
    }

    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final CharArrayWrapper that = (CharArrayWrapper)o;

        return this.length == that.length && CodecUtil.equals(values, that.values, this.length);
    }

    public String toString()
    {
        return new String(values, 0, length);
    }

    public int hashCode()
    {
        return hashcode;
    }
}