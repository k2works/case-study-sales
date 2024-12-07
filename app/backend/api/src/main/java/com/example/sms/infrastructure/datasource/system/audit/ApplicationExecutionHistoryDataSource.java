package com.example.sms.infrastructure.datasource.system.audit;

import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryList;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.infrastructure.datasource.ObjectOptimisticLockingFailureException;
import com.example.sms.service.system.audit.AuditRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationExecutionHistoryDataSource implements AuditRepository {
    final ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper;
    final ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper;

    public ApplicationExecutionHistoryDataSource(ApplicationExecutionHistoryMapper applicationExecutionHistoryMapper, ApplicationExecutionHistoryEntityMapper applicationExecutionHistoryEntityMapper) {
        this.applicationExecutionHistoryMapper = applicationExecutionHistoryMapper;
        this.applicationExecutionHistoryEntityMapper = applicationExecutionHistoryEntityMapper;
    }

    @Override
    public void deleteAll() {
        applicationExecutionHistoryMapper.deleteAll();
    }

    @Override
    public void save(ApplicationExecutionHistory history) {
        Optional<ApplicationExecutionHistoryEntity> historyEntity = Optional.ofNullable(applicationExecutionHistoryMapper.selectByPrimaryKey(history.getId()));
        if (historyEntity.isEmpty()) {
            ApplicationExecutionHistoryEntity newHistoryEntity = applicationExecutionHistoryEntityMapper.mapToEntity(history);
            if (newHistoryEntity.getId() != null) {
                applicationExecutionHistoryMapper.insert(newHistoryEntity);
            } else {
                applicationExecutionHistoryMapper.insertSelective(newHistoryEntity);
            }
        } else {
            ApplicationExecutionHistoryEntity historyEntityToUpdate = applicationExecutionHistoryEntityMapper.mapToEntity(history);
            int updateCount = applicationExecutionHistoryMapper.updateByPrimaryKey(historyEntityToUpdate);
            if (updateCount == 0) {
                throw new ObjectOptimisticLockingFailureException(ApplicationExecutionHistoryEntity.class, history.getId());
            }
        }
    }

    @Override
    public ApplicationExecutionHistoryList selectAll() {
        List<ApplicationExecutionHistoryEntity> applicationExecutionHistories = applicationExecutionHistoryMapper.selectAll();
        return new ApplicationExecutionHistoryList(applicationExecutionHistories.stream()
                .map(applicationExecutionHistoryEntityMapper::mapToDomainModel)
                .toList());
    }

    @Override
    public Optional<ApplicationExecutionHistory> findById(Integer id) {
        Optional<ApplicationExecutionHistoryEntity> applicationExecutionHistoryEntity = Optional.ofNullable(applicationExecutionHistoryMapper.selectByPrimaryKey(id));
        return applicationExecutionHistoryEntity.map(applicationExecutionHistoryEntityMapper::mapToDomainModel);
    }

    @Override
    public void deleteById(Integer id) {
        applicationExecutionHistoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ApplicationExecutionHistory> selectAllWithPageInfo() {
        List<ApplicationExecutionHistoryEntity> applicationExecutionHistories = applicationExecutionHistoryMapper.selectAll();
        PageInfo<ApplicationExecutionHistoryEntity> pageInfo = new PageInfo<>(applicationExecutionHistories);
        return PageInfoHelper.of(pageInfo, applicationExecutionHistoryEntityMapper::mapToDomainModel);
    }
}
