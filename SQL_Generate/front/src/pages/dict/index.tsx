import DictCard from '@/components/DictCard';
import { listMyDictByPage } from '@/services/dictService';
import { PageContainer } from '@ant-design/pro-components';
import { Col, message, Row } from 'antd';
import React from 'react';
import './index.less';

const IndexPage: React.FC = () => {
  const loadMyData = (
    searchParams: DictType.DictQueryRequest,
    setDataList: (dataList: DictType.Dict[]) => void,
    setTotal: (total: number) => void,
  ) => {
    listMyDictByPage(searchParams)
      .then((res) => {
        setDataList(res.data.records);
        setTotal(res.data.total);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  return (
    <div id="indexPage" className="dict-page">
      <Row gutter={[24, 24]} className="dict-container">
        <Col xs={24} xl={12}>
          <DictCard title="公开词库" showTag={false} />
        </Col>
        <Col xs={24} xl={12}>
          <DictCard title="个人词库" onLoad={loadMyData} needLogin />
        </Col>
      </Row>
    </div>
  );
};

export default IndexPage;
