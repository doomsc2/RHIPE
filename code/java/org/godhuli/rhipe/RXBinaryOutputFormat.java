/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.godhuli.rhipe;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.*;


public class RXBinaryOutputFormat extends FileOutputFormat<RHBytesWritable, RHBytesWritable> {

  public RecordWriter<RHBytesWritable, RHBytesWritable>
         getRecordWriter(TaskAttemptContext context
                         ) throws IOException, InterruptedException {
    Configuration conf = context.getConfiguration();
    
    // get the path of the temporary output file 
//     final int flushwhen = conf.getInt("rhipe.binaryoutput.splitsize", 32*1024*1024);
//     final boolean writemeta = conf.getInt("rhipe.binaryoutput.writemeta",0)==1? true:false;
    Path file = getDefaultWorkFile(context, "");
    FileSystem fs = file.getFileSystem(conf);
    final  FSDataOutputStream out = fs.create(file, false);
//     final  FSDataOutputStream metaout = writemeta? fs.create(new Path(file.getParent(),"meta-"+file.getName()), false): null;
//     if(writemeta) metaout.writeInt(0);
    return new RecordWriter<RHBytesWritable, RHBytesWritable>() {
	int counter =0;
        public void write(RHBytesWritable key, RHBytesWritable value)
          throws IOException {
	    // int kl,vl;
	    // byte[] kb,vb;
// 	    if( (out.size() - counter) >= flushwhen && writemeta){
// 		counter = out.size();
// 		metaout.writeInt(counter);
// 		metaout.sync();
// 	    }
	    key.writeAsInt(out);
	    value.writeAsInt(out);
	    // kl = key.getSize(); vl = value.getSize();
	    // kb = key.getBytes(); vb=value.getBytes();
	    // out.writeInt(kl);out.write(kb,0,kl);
	    // out.writeInt(vl);out.write(vb,0,vl);
	    out.sync();
        }

        public void close(TaskAttemptContext context) throws IOException { 
          out.close();
// 	  if(writemeta) metaout.close();
        }
      };
  }
}


// public class RXBinaryOutputFormat extends FileOutputFormat<RHBytesWritable, RHBytesWritable> {

//   public RecordWriter<RHBytesWritable, RHBytesWritable>
//          getRecordWriter(TaskAttemptContext context
//                          ) throws IOException, InterruptedException {
//     Configuration conf = context.getConfiguration();
    
//     // get the path of the temporary output file 
//     Path file = getDefaultWorkFile(context, "");
//     FileSystem fs = file.getFileSystem(conf);
//     final  FSDataOutputStream out = fs.create(file, false);

//     return new RecordWriter<RHBytesWritable, RHBytesWritable>() {

//         public void write(RHBytesWritable key, RHBytesWritable value)
//           throws IOException {
// 	    int kl,vl;
// 	    byte[] kb,vb;
// 	    kl = key.getLength(); vl = value.getLength();
// 	    kb = key.get(); vb=value.get();
// 	    out.writeInt(kl);out.write(kb,0,kl);
// 	    out.writeInt(vl);out.write(vb,0,vl);
// 	    out.sync();
//         }

//         public void close(TaskAttemptContext context) throws IOException { 
//           out.close();
//         }
//       };
//   }
// }
