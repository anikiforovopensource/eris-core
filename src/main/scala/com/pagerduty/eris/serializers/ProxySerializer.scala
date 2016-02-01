/*
 * Copyright (c) 2015, PagerDuty
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.pagerduty.eris.serializers

import java.nio.ByteBuffer

import com.netflix.astyanax.Serializer
import com.netflix.astyanax.serializers.{ AbstractSerializer, ComparatorType }

/**
 * Generic serializer that delegate serialization of type T to serializer of type R using
 * conversions. Extend this when you want delegate all the work to an existing serializer.
 */
class ProxySerializer[T, R](
  protected val toRepresentation: T => R,
  protected val fromRepresentation: R => T,
  protected val serializer: Serializer[R]
)
    extends AbstractSerializer[T] with ValidatorClass {
  def toByteBuffer(obj: T): ByteBuffer = serializer.toByteBuffer(toRepresentation(obj))
  def fromByteBuffer(bytes: ByteBuffer): T = fromRepresentation(serializer.fromByteBuffer(bytes))
  override def getComparatorType(): ComparatorType = serializer.getComparatorType
  override def fromString(string: String): ByteBuffer = serializer.fromString(string)
  override def getString(bytes: ByteBuffer): String = serializer.getString(bytes)
  val validatorClass: String = ValidatorClass(serializer)
}
