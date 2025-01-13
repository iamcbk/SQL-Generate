import FieldInfoCard from '@/components/FieldInfoCard';
import { listMyAddFieldInfoByPage } from '@/services/fieldInfoService';
import { PageContainer } from '@ant-design/pro-components';
import { Col, message, Row } from 'antd';
import React from 'react';
import './index.less';

const FieldInfoPage: React.FC = () => {
  const loadMyData = (
    searchParams: FieldInfoType.FieldInfoQueryRequest,
    setDataList: (dataList: FieldInfoType.FieldInfo[]) => void,
    setTotal: (total: number) => void,
  ) => {
    listMyAddFieldInfoByPage(searchParams)
      .then((res) => {
        setDataList(res.data.records);
        setTotal(res.data.total);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  return (
    <div className="field-info">
      <PageContainer ghost>
        <Row gutter={[24, 24]}>
          <Col xs={24} xl={12}>
            <FieldInfoCard 
              title="公开字段" 
              showTag={false}
              type="public" 
            />
          </Col>
          <Col xs={24} xl={12}>
            <FieldInfoCard 
              title="个人字段" 
              onLoad={loadMyData} 
              needLogin
              type="private"
            />
          </Col>
        </Row>
      </PageContainer>
    </div>
  );
};

export default FieldInfoPage;
