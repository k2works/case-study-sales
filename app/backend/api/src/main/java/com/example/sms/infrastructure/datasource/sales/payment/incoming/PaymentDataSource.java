package com.example.sms.infrastructure.datasource.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.autogen.mapper.入金データMapper;
import com.example.sms.infrastructure.datasource.autogen.model.入金データ;
import com.example.sms.service.sales.payment.incoming.PaymentRepository;
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
public class PaymentDataSource implements PaymentRepository {
    private final 入金データMapper paymentMapper;
    private final PaymentCustomMapper paymentCustomMapper;
    private final PaymentEntityMapper paymentEntityMapper;

    public PaymentDataSource(
            入金データMapper paymentMapper,
            PaymentCustomMapper paymentCustomMapper,
            PaymentEntityMapper paymentEntityMapper) {
        this.paymentMapper = paymentMapper;
        this.paymentCustomMapper = paymentCustomMapper;
        this.paymentEntityMapper = paymentEntityMapper;
    }

    @Override
    public void deleteAll() {
        paymentCustomMapper.deleteAll();
    }

    @Override
    public void save(Payment payment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null && authentication.getName() != null ? authentication.getName() : "system";
        Optional<PaymentCustomEntity> paymentEntity = Optional.ofNullable(paymentCustomMapper.selectByPrimaryKey(payment.getPaymentNumber().getValue()));

        if (paymentEntity.isEmpty()) {
            createPayment(payment, username);
        } else {
            updatePayment(payment, paymentEntity, username);
        }
    }

    private void createPayment(Payment payment, String username) {
        入金データ entity = paymentEntityMapper.mapToEntity(payment);
        entity.set作成日時(LocalDateTime.now());
        entity.set作成者名(username);
        entity.set更新日時(LocalDateTime.now());
        entity.set更新者名(username);
        paymentMapper.insert(entity);
    }

    private void updatePayment(Payment payment, Optional<PaymentCustomEntity> paymentEntity, String username) {
        入金データ entity = paymentEntityMapper.mapToEntity(payment);
        if (paymentEntity.isPresent()) {
            entity.set作成日時(paymentEntity.get().get作成日時());
            entity.set作成者名(paymentEntity.get().get作成者名());
            entity.set更新日時(LocalDateTime.now());
            entity.set更新者名(username);
        }

        paymentMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<Payment> selectAll() {
        List<PaymentCustomEntity> entities = paymentCustomMapper.selectAll();
        return entities.stream()
                .map(paymentEntityMapper::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Payment> findById(String paymentNumber) {
        PaymentCustomEntity entity = paymentCustomMapper.selectByPrimaryKey(paymentNumber);
        return Optional.ofNullable(entity).map(paymentEntityMapper::mapToDomainModel);
    }

    @Override
    public void delete(Payment payment) {
        paymentMapper.deleteByPrimaryKey(payment.getPaymentNumber().getValue());
    }

    @Override
    public PageInfo<Payment> selectAllWithPageInfo() {
        List<PaymentCustomEntity> entities = paymentCustomMapper.selectAll();
        PageInfo<PaymentCustomEntity> pageInfo = new PageInfo<>(entities);
        return PageInfoHelper.of(pageInfo, paymentEntityMapper::mapToDomainModel);
    }

    @Override
    public List<Payment> findByCustomer(String customerCode, Integer branchNumber) {
        List<PaymentCustomEntity> entities = paymentCustomMapper.selectByCustomer(customerCode, branchNumber);
        return entities.stream()
                .map(paymentEntityMapper::mapToDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByAccount(String accountCode) {
        List<PaymentCustomEntity> entities = paymentCustomMapper.selectByAccount(accountCode);
        return entities.stream()
                .map(paymentEntityMapper::mapToDomainModel)
                .collect(Collectors.toList());
    }
}
