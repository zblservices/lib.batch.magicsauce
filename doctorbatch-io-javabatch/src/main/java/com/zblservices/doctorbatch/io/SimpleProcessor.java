/*
 * Copyright 2016 ZBL Services, Inc.
 * Copyright 2015 IBM Corp.
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

package com.zblservices.doctorbatch.io;

import static com.zblservices.doctorbatch.io.Constants.RECORD_PROCESSOR;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.zblservices.doctorbatch.io.ClassUtil;
import com.zblservices.doctorbatch.io.RecordProcessor;

/**
 * ETLProcessor is a JSR-352 ItemProcessor implementation that utilizes a RecordProcessBehavior
 * implementation, specified by the RECORD_PROCESS_BEHAVIOR job step property, for its implementation.
 * 
 * @author Timothy C. Fanelli (tfanelli@zblservices.com, tim@fanel.li)
 *
 * @param <R> The input record type
 * @param <P> The output, processed record type
 */
public class SimpleProcessor<R,P> implements ItemProcessor {
	private RecordProcessor<R, P> recordProcessor;
	
	@Inject StepContext stepContext;
	@Inject @BatchProperty(name=RECORD_PROCESSOR) private String processorClassName;


	@SuppressWarnings("rawtypes")
	private static ThreadLocal<SimpleProcessor> processorInstance =
			new ThreadLocal<SimpleProcessor>();
	
	public SimpleProcessor() {
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object processItem(Object record) throws Exception {
		if ( recordProcessor == null ) {
			try {
				recordProcessor = ClassUtil.getInstanceForClass( processorClassName );
				recordProcessor.initialize(stepContext.getProperties());
			} catch ( Throwable t ) {
				t.printStackTrace();
				System.out.println( t.getMessage() );
			}
		}

		return (P) recordProcessor.process((R) record);
	}

	static SimpleProcessor<?,?> getDMBSProcessor() {
		return processorInstance.get();
	}
	
	void setRecordProcessor( RecordProcessor<R, P> processor ) {
		this.recordProcessor = processor;
	}
	
	void removeProcessorInstance() {
		processorInstance.set(null);
	}
}
