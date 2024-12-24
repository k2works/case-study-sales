package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.infrastructure.datasource.autogen.mapper.ApplicationExecutionHistoryMapper;
import com.example.sms.service.system.audit.AuditRepository;
import com.example.sms.service.system.audit.AuditCriteria;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationExecutionHistoryDataSource implements AuditRepository {
    final ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper;
    final ApplicationExecutionHistoryCustomMapper applicationExecutionHistoryCustomMapper;
    final ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper;

    public ApplicationExecutionHistoryDataSource(ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper, ApplicationExecutionHistoryCustomMapper applicationExecutionHistoryCustomMapper, ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper) {
        this.applicationExecutionHistoryMapper = applicationExecutionHistoryMapper;
        this.applicationExecutionHistoryCustomMapper = applicationExecutionHistoryCustomMapper;
        this.applicationExecutionHistoryEntityMapper = applicationExecutionHistoryEntityMapper;
    }

    @Override
    public void deleteAll() {
        applicationExecutionHistoryCustomMapper.deleteAll();
    }

    @Override
    public void save(ApplicationExecutionHistory history) {
        Optional<ApplicationExecutionHistoryCustomEntity> historyEntity = Optional.ofNullable(applicationExecutionHistoryCustomMapper.selectByPrimaryKey(history.getId()));
        if (historyEntity.isEmpty()) {
            ApplicationExecutionHistoryCustomEntity newHistoryEntity = applicationExecutionHistoryEntityMapper.mapToEntity(history);
            if (newHistoryEntity.getId() != null) {
                applicationExecutionHistoryMapper.insert(newHistoryEntity);
            } else {
                applicationExecutionHistoryMapper.insertSelective(newHistoryEntity);
            }
        } else {
            ApplicationExecutionHistoryCustomEntity historyEntityToUpdate = applicationExecutionHistoryEntityMapper.mapToEntity(history);
            int updateCount = applicationExecutionHistoryMapper.updateByPrimaryKey(historyEntityToUpdate);
            if (updateCount == 0) {
                throw new ObjectOptimisticLockingFailureException(ApplicationExecutionHistoryCustomEntity.class, history.getId());
            }
        }
    }

    @Override
    public ApplicationExecutionHistoryList selectAll() {
        List<ApplicationExecutionHistoryCustomEntity> applicationExecutionHistories = applicationExecutionHistoryCustomMapper.selectAll();
        return new ApplicationExecutionHistoryList(applicationExecutionHistories.stream()
                .map(applicationExecutionHistoryEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<ApplicationExecutionHistory> findById(Integer id) {
        Optional<ApplicationExecutionHistoryCustomEntity> applicationExecutionHistoryEntity = Optional.ofNullable(applicationExecutionHistoryCustomMapper.selectByPrimaryKey(id));
        return applicationExecutionHistoryEntity.map(applicationExecutionHistoryEntityMapper::mapToDomainModel);
    }

    @Override
    public void deleteById(Integer id) {
        applicationExecutionHistoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ApplicationExecutionHistory> selectAllWithPageInfo() {
        List<ApplicationExecutionHistoryCustomEntity> applicationExecutionHistories = applicationExecutionHistoryCustomMapper.selectAll();
        PageInfo<ApplicationExecutionHistoryCustomEntity> pageInfo = new PageInfo<>(applicationExecutionHistories);
        return PageInfoHelper.of(pageInfo, applicationExecutionHistoryEntityMapper::mapToDomainModel);
    }

    @Override
    public ApplicationExecutionHistory start(ApplicationExecutionHistory history) {
        ApplicationExecutionHistoryCustomEntity newHistoryEntity = applicationExecutionHistoryEntityMapper.mapToEntity(history);
        applicationExecutionHistoryCustomMapper.insertForStart(newHistoryEntity);
        return applicationExecutionHistoryEntityMapper.mapToDomainModel(newHistoryEntity);
    }

    @Override
    public PageInfo<ApplicationExecutionHistory> searchWithPageInfo(AuditCriteria criteria) {
        List<ApplicationExecutionHistoryCustomEntity> applicationExecutionHistories = applicationExecutionHistoryCustomMapper.selectByCriteria(criteria);
        PageInfo<ApplicationExecutionHistoryCustomEntity> pageInfo = new PageInfo<>(applicationExecutionHistories);
        return PageInfoHelper.of(pageInfo, applicationExecutionHistoryEntityMapper::mapToDomainModel);
    }
}
