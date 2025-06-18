package com.example.sms.infrastructure.datasource.master.payment.account;

import com.example.sms.domain.model.master.payment.account.PaymentAccount;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.入金口座マスタMapper;
import com.example.sms.infrastructure.datasource.autogen.model.入金口座マスタ;
import com.example.sms.service.master.payment.PaymentAccountRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 入金口座のリポジトリ実装
 */
@Repository
public class PaymentAccountDataSource implements PaymentAccountRepository {
    private final 入金口座マスタMapper accountMapper;
    private final PaymentAccountCustomMapper accountCustomMapper;
    private final PaymentAccountEntityMapper paymentAccountEntityMapper;
    private final PaymentAccountCustomMapper paymentAccountCustomMapper;

    public PaymentAccountDataSource(
            入金口座マスタMapper accountMapper,
            PaymentAccountEntityMapper paymentAccountEntityMapper,
            PaymentAccountCustomMapper paymentAccountCustomMapper) {
        this.accountMapper = accountMapper;
        this.accountCustomMapper = paymentAccountCustomMapper;
        this.paymentAccountEntityMapper = paymentAccountEntityMapper;
        this.paymentAccountCustomMapper = paymentAccountCustomMapper;
    }

    @Override
    public void deleteAll() {
        paymentAccountCustomMapper.deleteAll();
    }

    @Override
    public void save(PaymentAccount paymentAccount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        Optional<PaymentAccountCustomEntity> accountEntity = Optional.ofNullable(accountCustomMapper.selectByPrimaryKey(paymentAccount.getAccountCode().getValue()));

        if (accountEntity.isEmpty()) {
            createAccount(paymentAccount, username);
        } else {
            updateAccount(paymentAccount, accountEntity, username);
        }
    }

    @Override
    public List<PaymentAccount> selectAll() {
        List<PaymentAccountCustomEntity> entities = accountCustomMapper.selectAll();
        return entities.stream()
                .map(paymentAccountEntityMapper::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentAccount> findById(String accountCode) {
        PaymentAccountCustomEntity entity = accountCustomMapper.selectByPrimaryKey(accountCode);
        return Optional.ofNullable(entity).map(paymentAccountEntityMapper::mapToDomainModel);
    }

    @Override
    public void delete(PaymentAccount paymentAccount) {
        accountMapper.deleteByPrimaryKey(paymentAccount.getAccountCode().getValue());
    }

    @Override
    public PageInfo<PaymentAccount> selectAllWithPageInfo() {
        List<PaymentAccountCustomEntity> entities = paymentAccountCustomMapper.selectAll();
        PageInfo<PaymentAccountCustomEntity> pageInfo = new PageInfo<>(entities);
        return PageInfoHelper.of(pageInfo, paymentAccountEntityMapper::mapToDomainModel);
    }

    private void createAccount(PaymentAccount paymentAccount, String username) {
        入金口座マスタ entity = paymentAccountEntityMapper.mapToEntity(paymentAccount);
        entity.set作成日時(LocalDateTime.now());
        entity.set作成者名(username);
        entity.set更新日時(LocalDateTime.now());
        entity.set更新者名(username);
        accountMapper.insert(entity);
    }

    private void updateAccount(PaymentAccount paymentAccount, Optional<PaymentAccountCustomEntity> accountEntity, String username) {
        入金口座マスタ entity = paymentAccountEntityMapper.mapToEntity(paymentAccount);
        if (accountEntity.isPresent()) {
            entity.set作成日時(accountEntity.get().get作成日時());
            entity.set作成者名(accountEntity.get().get作成者名());
            entity.set更新日時(LocalDateTime.now());
            entity.set更新者名(username);
        }

        accountMapper.updateByPrimaryKeySelective(entity);
    }

}
