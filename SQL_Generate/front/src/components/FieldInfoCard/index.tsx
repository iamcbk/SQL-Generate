import FieldInfoList from '@/components/FieldInfoList';
import { listFieldInfoByPage } from '@/services/fieldInfoService';
import { Link, useModel } from '@umijs/max';
import { Button, Card, Empty, Input, message, Space, Typography } from 'antd';
import React, { useEffect, useState } from 'react';
import './index.less';
import { SearchOutlined, PlusOutlined, DatabaseOutlined } from '@ant-design/icons';
import FieldInfoCreateModal from '../FieldInfoModal/FieldInfoCreateModal';

// 默认分页大小
const DEFAULT_PAGE_SIZE = 10;

interface Props {
  title?: string;
  needLogin?: boolean;
  showTag?: boolean;
  type?: 'public' | 'private';
  onLoad?: (
    searchParams: FieldInfoType.FieldInfoQueryRequest,
    setDataList: (dataList: FieldInfoType.FieldInfo[]) => void,
    setTotal: (total: number) => void,
  ) => void;
  onImport?: (values: FieldInfoType.FieldInfo) => void;
}

const FieldInfoCard: React.FC<Props> = (props) => {
  const { 
    title = '字段信息列表', 
    needLogin = false, 
    showTag = true, 
    type = 'public',
    onLoad, 
    onImport 
  } = props;

  // 公开数据
  const [dataList, setDataList] = useState<FieldInfoType.FieldInfo[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);
  const initSearchParams: FieldInfoType.FieldInfoQueryRequest = {
    current: 1,
    pageSize: DEFAULT_PAGE_SIZE,
    sortField: 'createTime',
    sortOrder: 'descend',
  };
  const [searchParams, setSearchParams] =
    useState<FieldInfoType.FieldInfoQueryRequest>(initSearchParams);

  const { initialState } = useModel('@@initialState');
  const loginUser = initialState?.loginUser;

  // 添加新的 state
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);

  /**
   * 加载数据
   */
  const innerOnLoad = () => {
    listFieldInfoByPage({
      ...searchParams,
      // 只展示已审核通过的
      reviewStatus: 1,
    })
      .then((res) => {
        setDataList(res.data.records);
        setTotal(res.data.total);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  // 加载数据
  useEffect(() => {
    // 需要登录
    if (needLogin && !loginUser) {
      return;
    }
    setLoading(true);
    if (onLoad) {
      onLoad(searchParams, setDataList, setTotal);
    } else {
      innerOnLoad();
    }
    setLoading(false);
  }, [searchParams]);

  return (
    <div className={`field-info-card ${type}`}>
      <Card
        title={
          <Space>
            <DatabaseOutlined className="card-icon" />
            <Typography.Title level={5} style={{ marginBottom: 0 }}>
              {title}
            </Typography.Title>
          </Space>
        }
        extra={
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={() => setCreateModalVisible(true)}
          >
            新建
          </Button>
        }
        bordered={false}
        className="custom-card"
      >
        {!needLogin || loginUser ? (
          <>
            <Input.Search
              placeholder="搜索字段名称..."
              enterButton={
                <Button type="primary" icon={<SearchOutlined />}>
                  搜索
                </Button>
              }
              size="large"
              allowClear
              className="search-input"
              onSearch={(value) => {
                setSearchParams({
                  ...initSearchParams,
                  searchName: value,
                });
              }}
            />
            <FieldInfoList
              pagination={{
                total,
                onChange: (current) => {
                  setSearchParams({ ...searchParams, current });
                  window.scrollTo({
                    top: 0,
                  });
                },
                pageSize: DEFAULT_PAGE_SIZE,
              }}
              dataList={dataList}
              loading={loading}
              showTag={showTag}
              onImport={onImport}
            />
          </>
        ) : (
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description={
              <Space direction="vertical" align="center">
                <Typography.Text type="secondary">
                  请登录后查看个人字段
                </Typography.Text>
                <Link to="/user/login">
                  <Button type="primary" size="large">
                    立即登录
                  </Button>
                </Link>
              </Space>
            }
          />
        )}
        
        {/* 添加创建模态框 */}
        <FieldInfoCreateModal
          modalVisible={createModalVisible}
          onSubmit={() => {
            setCreateModalVisible(false);
            // 刷新数据
            if (onLoad) {
              onLoad(searchParams, setDataList, setTotal);
            } else {
              innerOnLoad();
            }
          }}
          onCancel={() => setCreateModalVisible(false)}
        />
      </Card>
    </div>
  );
};

export default FieldInfoCard;
