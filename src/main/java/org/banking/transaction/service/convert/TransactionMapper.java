package org.banking.transaction.service.convert;

import org.banking.transaction.common.constants.TransactionStatus;
import org.banking.transaction.repository.entity.Transaction;
import org.banking.transaction.request.TransactionCreateVO;
import org.banking.transaction.response.TransactionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    default long map(LocalDateTime localDateTime) {
        return localDateTime != null
                ? localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                : 0L;
    }


    @Mappings({
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    })
    Transaction createVoToEntity(TransactionCreateVO vo, TransactionStatus status);

    @Mappings({
            @Mapping(target = "amount", source = "amountStr")
    })
    TransactionVO entityToVo(Transaction entity,String amountStr);
}
