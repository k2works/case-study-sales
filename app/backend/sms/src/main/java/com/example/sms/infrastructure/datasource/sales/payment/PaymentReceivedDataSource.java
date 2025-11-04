package com.example.sms.infrastructure.datasource.sales.payment;

import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.domain.model.sales.payment.PaymentReceivedList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.入金データMapper;
import com.example.sms.infrastructure.datasource.autogen.model.入金データ;
import com.example.sms.service.sales.payment.PaymentReceivedCriteria;
import com.example.sms.service.sales.payment.PaymentReceivedRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 入金データのリポジトリ実装
 */
@Repository
public class PaymentReceivedDataSource implements PaymentReceivedRepository {
    private final 入金データMapper paymentReceivedMapper;
    private final PaymentReceivedCustomMapper paymentReceivedCustomMapper;
    private final PaymentReceivedEntityMapper paymentReceivedEntityMapper;

    public PaymentReceivedDataSource(
            入金データMapper paymentReceivedMapper,
            PaymentReceivedCustomMapper paymentReceivedCustomMapper,
            PaymentReceivedEntityMapper paymentReceivedEntityMapper) {
        this.paymentReceivedMapper = paymentReceivedMapper;
        this.paymentReceivedCustomMapper = paymentReceivedCustomMapper;
        this.paymentReceivedEntityMapper = paymentReceivedEntityMapper;
    }

    @Override
    public void deleteAll() {
        paymentReceivedCustomMapper.deleteAll();
    }

    @Override
    public void save(PaymentReceived paymentReceived) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        Optional<PaymentReceivedCustomEntity> paymentReceivedEntity = Optional.ofNullable(paymentReceivedCustomMapper.selectByPrimaryKey(paymentReceived.getPaymentNumber().getValue()));

        if (paymentReceivedEntity.isEmpty()) {
            createPaymentReceived(paymentReceived, username);
        } else {
            updatePaymentReceived(paymentReceived, paymentReceivedEntity, username);
        }
    }

    private void createPaymentReceived(PaymentReceived paymentReceived, String username) {
        入金データ entity = paymentReceivedEntityMapper.mapToEntity(paymentReceived);
        entity.set作成日時(LocalDateTime.now());
        entity.set作成者名(username);
        entity.set更新日時(LocalDateTime.now());
        entity.set更新者名(username);
        paymentReceivedMapper.insert(entity);
    }

    private void updatePaymentReceived(PaymentReceived paymentReceived, Optional<PaymentReceivedCustomEntity> paymentReceivedEntity, String username) {
        入金データ entity = paymentReceivedEntityMapper.mapToEntity(paymentReceived);
        if (paymentReceivedEntity.isPresent()) {
            entity.set作成日時(paymentReceivedEntity.get().get作成日時());
            entity.set作成者名(paymentReceivedEntity.get().get作成者名());
            entity.set更新日時(LocalDateTime.now());
            entity.set更新者名(username);
        }

        paymentReceivedMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public PaymentReceivedList selectAll() {
        List<PaymentReceivedCustomEntity> entities = paymentReceivedCustomMapper.selectAll();
        return new PaymentReceivedList(entities.stream()
                .map(paymentReceivedEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<PaymentReceived> findById(String paymentReceivedNumber) {
        PaymentReceivedCustomEntity entity = paymentReceivedCustomMapper.selectByPrimaryKey(paymentReceivedNumber);
        return Optional.ofNullable(entity).map(paymentReceivedEntityMapper::mapToDomainModel);
    }

    @Override
    public void delete(PaymentReceived paymentReceived) {
        paymentReceivedMapper.deleteByPrimaryKey(paymentReceived.getPaymentNumber().getValue());
    }

    @Override
    public PageInfo<PaymentReceived> selectAllWithPageInfo() {
        List<PaymentReceivedCustomEntity> entities = paymentReceivedCustomMapper.selectAll();
        PageInfo<PaymentReceivedCustomEntity> pageInfo = new PageInfo<>(entities);
        return PageInfoHelper.of(pageInfo, paymentReceivedEntityMapper::mapToDomainModel);
    }

    @Override
    public PageInfo<PaymentReceived> searchWithPageInfo(PaymentReceivedCriteria criteria) {
        List<PaymentReceivedCustomEntity> entities = paymentReceivedCustomMapper.selectByCriteria(criteria);
        PageInfo<PaymentReceivedCustomEntity> pageInfo = new PageInfo<>(entities);
        return PageInfoHelper.of(pageInfo, paymentReceivedEntityMapper::mapToDomainModel);
    }

    @Override
    public List<PaymentReceived> findByCustomer(String customerCode, Integer branchNumber) {
        List<PaymentReceivedCustomEntity> entities = paymentReceivedCustomMapper.selectByCustomer(customerCode, branchNumber);
        return entities.stream()
                .map(paymentReceivedEntityMapper::mapToDomainModel)
                .toList();
    }

    @Override
    public List<PaymentReceived> findByAccount(String accountCode) {
        List<PaymentReceivedCustomEntity> entities = paymentReceivedCustomMapper.selectByAccount(accountCode);
        return entities.stream()
                .map(paymentReceivedEntityMapper::mapToDomainModel)
                .toList();
    }
}
