package com.example.sms.infrastructure.datasource.system.download;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountList;
import com.example.sms.domain.model.system.download.DownloadCriteria;
import com.example.sms.infrastructure.datasource.master.payment.account.incoming.PaymentAccountCustomEntity;
import com.example.sms.infrastructure.datasource.master.payment.account.incoming.PaymentAccountCustomMapper;
import com.example.sms.infrastructure.datasource.master.payment.account.incoming.PaymentAccountEntityMapper;
import com.example.sms.service.system.download.PaymentAccountCSVRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentAccountCSVDataSource implements PaymentAccountCSVRepository {
    private final PaymentAccountCustomMapper paymentAccountCustomMapper;
    private final PaymentAccountEntityMapper paymentAccountEntityMapper;

    // コンストラクタ
    public PaymentAccountCSVDataSource(PaymentAccountCustomMapper paymentAccountCustomMapper, PaymentAccountEntityMapper paymentAccountEntityMapper) {
        this.paymentAccountCustomMapper = paymentAccountCustomMapper;
        this.paymentAccountEntityMapper = paymentAccountEntityMapper;
    }

    @Override
    public List<PaymentAccountDownloadCSV> convert(PaymentAccountList paymentAccountList) {
        if (paymentAccountList != null) {
            return paymentAccountList.asList().stream()
                    .map(this::mapToCsvModel)
                    .toList();
        }
        return List.of();
    }

    @Override
    public int countBy(DownloadCriteria condition) {
        List<PaymentAccountCustomEntity> paymentAccountEntities = paymentAccountCustomMapper.selectAll();
        return paymentAccountEntities.size();
    }

    @Override
    public PaymentAccountList selectBy(DownloadCriteria condition) {
        List<PaymentAccountCustomEntity> paymentAccountEntities = paymentAccountCustomMapper.selectAll();
        return new PaymentAccountList(paymentAccountEntities.stream()
                .map(paymentAccountEntityMapper::mapToDomainModel)
                .toList());
    }

    // PaymentAccount から PaymentAccountDownloadCSV へのマッピング
    private PaymentAccountDownloadCSV mapToCsvModel(PaymentAccount paymentAccount) {
        return new PaymentAccountDownloadCSV(
                paymentAccount.getAccountCode().getValue(),
                paymentAccount.getAccountName(),
                paymentAccount.getStartDate(),
                paymentAccount.getEndDate(),
                paymentAccount.getAccountNameAfterStart(),
                paymentAccount.getAccountType().getCode(),
                paymentAccount.getAccountNumber(),
                paymentAccount.getBankAccountType().getCode(),
                paymentAccount.getAccountHolder(),
                paymentAccount.getDepartmentId().getDeptCode().getValue(),
                paymentAccount.getBankCode(),
                paymentAccount.getBranchCode()
        );
    }
}