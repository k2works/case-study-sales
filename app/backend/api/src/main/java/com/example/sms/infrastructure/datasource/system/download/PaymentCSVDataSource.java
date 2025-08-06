package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.sales.payment.incoming.PaymentList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentCustomEntity;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentCustomMapper;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentEntityMapper;
import com.example.sms.service.system.download.PaymentCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentCSVDataSource implements PaymentCSVRepository {
    private final PaymentCustomMapper paymentCustomMapper;
    private final PaymentEntityMapper paymentEntityMapper;

    // コンストラクタ
    public PaymentCSVDataSource(PaymentCustomMapper paymentCustomMapper, PaymentEntityMapper paymentEntityMapper) {
        this.paymentCustomMapper = paymentCustomMapper;
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @Override
    public List<PaymentDownloadCSV> convert(PaymentList paymentList) {
        if (paymentList != null) {
            return paymentList.asList().stream()
                    .map(this::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PaymentCustomEntity> paymentEntities = paymentCustomMapper.selectAll();
        return paymentEntities.size();
    }

    @Override
    public PaymentList selectBy(DownloadCriteria condition) {
        List<PaymentCustomEntity> paymentEntities = paymentCustomMapper.selectAll();
        return new PaymentList(paymentEntities.stream()
                .map(paymentEntityMapper::mapToDomainModel)
                .toList());
    }

    // Payment から PaymentDownloadCSV へのマッピング
    private PaymentDownloadCSV mapToCsvModel(Payment payment) {
        return new PaymentDownloadCSV(
                payment.getPaymentNumber().getValue(),
                payment.getPaymentDate(),
                payment.getDepartmentId().getDeptCode().getValue(),
                payment.getCustomerCode().getCode().getValue(),
                payment.getCustomerCode().getBranchNumber(),
                payment.getPaymentMethodType().getCode(),
                payment.getPaymentAccountCode(),
                payment.getPaymentAmount().getAmount(),
                payment.getOffsetAmount().getAmount()
        );
    }
}
