/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.exec.operator.params;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dingodb.codec.CodecService;
import io.dingodb.codec.KeyValueCodec;
import io.dingodb.common.CommonId;
import io.dingodb.common.partition.RangeDistribution;
import io.dingodb.common.table.TableDefinition;
import io.dingodb.common.type.DingoType;
import io.dingodb.common.type.TupleMapping;
import io.dingodb.common.util.ByteArrayUtils;
import io.dingodb.exec.expr.SqlExpr;
import lombok.Getter;

import java.util.Map;
import java.util.NavigableMap;

@Getter
@JsonTypeName("partVector")
@JsonPropertyOrder({
    "tableId", "part", "schema", "keyMapping", "filter", "selection", "indexId", "indexRegionId"
})
public class PartVectorParam extends FilterProjectSourceParam {

    private final KeyValueCodec codec;
    private final TableDefinition tableDefinition;
    private final NavigableMap<ByteArrayUtils.ComparableByteArray, RangeDistribution> distributions;
    private CommonId indexId;
    private CommonId indexRegionId;
    private Float[] floatArray;
    private int topN;
    private Map<String, Object> parameterMap;

    public PartVectorParam(
        CommonId tableId,
        CommonId partId,
        DingoType schema,
        TupleMapping keyMapping,
        SqlExpr filter,
        TupleMapping selection,
        TableDefinition tableDefinition,
        NavigableMap<ByteArrayUtils.ComparableByteArray, RangeDistribution> distributions,
        CommonId indexId,
        CommonId indexRegionId,
        Float[] floatArray,
        int topN,
        Map<String, Object> parameterMap
    ) {
        super(tableId, partId, schema, filter, selection, keyMapping);
        this.codec = CodecService.getDefault().createKeyValueCodec(tableDefinition);
        this.tableDefinition = tableDefinition;
        this.distributions = distributions;
        this.indexId = indexId;
        this.indexRegionId = indexRegionId;
        this.floatArray = floatArray;
        this.topN = topN;
        this.parameterMap = parameterMap;
    }
}
