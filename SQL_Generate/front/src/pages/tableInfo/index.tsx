import TableInfoCard from '@/components/TableInfoCard';
import { listMyAddTableInfoByPage } from '@/services/tableInfoService';
import { PageContainer } from '@ant-design/pro-components';
import { useNavigate } from '@umijs/max';
import { Col, message, Radio, RadioChangeEvent, Row } from 'antd';
import React, { useState } from 'react';
import './index.less';

const TableInfoPage: React.FC = () => {
  const [layout, setLayout] = useState('half');

  const navigate = useNavigate();

  /**
   * 加载我的数据
   * @param searchParams
   * @param setDataList
   * @param setTotal
   */
  const loadMyData = (
    searchParams: TableInfoType.TableInfoQueryRequest,
    setDataList: (dataList: TableInfoType.TableInfo[]) => void,
    setTotal: (total: number) => void,
  ) => {
    listMyAddTableInfoByPage(searchParams)
      .then((res) => {
        setDataList(res.data.records);
        setTotal(res.data.total);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  // 导入表，跳转到主页
  const doImport = (tableInfo: TableInfoType.TableInfo) => {
    navigate(`/?table_id=${tableInfo.id}`);
  };

  /**
   * 更改布局
   * @param e
   */
  const onLayoutChange = (e: RadioChangeEvent) => {
    setLayout(e.target.value);
  };

  return (
    <div className="table-info">
      <Row gutter={[24, 24]}>
        <Col xs={24} xl={12}>
          <TableInfoCard
            title="公开表信息"
            showTag={false}
            onImport={doImport}
            type="public"
          />
        </Col>
        <Col xs={24} xl={12}>
          <TableInfoCard
            title="个人表"
            onImport={doImport}
            onLoad={loadMyData}
            needLogin
            type="private"
          />
        </Col>
      </Row>
    </div>
  );
};

export default TableInfoPage;
