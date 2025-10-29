package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.sales.payment.incoming.PaymentReceived;
import com.example.sms.domain.model.sales.payment.incoming.PaymentReceivedList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentReceivedCustomEntity;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentReceivedCustomMapper;
import com.example.sms.infrastructure.datasource.sales.payment.incoming.PaymentReceivedEntityMapper;
import com.example.sms.service.system.download.PaymentCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentCSVDataSource implements PaymentCSVRepository {
    private final PaymentReceivedCustomMapper paymentReceivedCustomMapper;
    private final PaymentReceivedEntityMapper paymentReceivedEntityMapper;

    // コンストラクタ
    public PaymentCSVDataSource(PaymentReceivedCustomMapper paymentReceivedCustomMapper, PaymentReceivedEntityMapper paymentReceivedEntityMapper) {
        this.paymentReceivedCustomMapper = paymentReceivedCustomMapper;
        this.paymentReceivedEntityMapper = paymentReceivedEntityMapper;
    }

    @Override
    public List<PaymentDownloadCSV> convert(PaymentReceivedList paymentReceivedList) {
        if (paymentReceivedList != null) {
            return paymentReceivedList.asList().stream()
                    .map(this::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PaymentReceivedCustomEntity> paymentReceivedEntities = paymentReceivedCustomMapper.selectAll();
        return paymentReceivedEntities.size();
    }

    @Override
    public PaymentReceivedList selectBy(DownloadCriteria condition) {
        List<PaymentReceivedCustomEntity> paymentReceivedEntities = paymentReceivedCustomMapper.selectAll();
        return new PaymentReceivedList(paymentReceivedEntities.stream()
                .map(paymentReceivedEntityMapper::mapToDomainModel)
                .toList());
    }

    // PaymentReceived から PaymentDownloadCSV へのマッピング
    private PaymentDownloadCSV mapToCsvModel(PaymentReceived payment) {
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
