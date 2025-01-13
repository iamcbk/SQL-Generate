import TableInfoList from '@/components/TableInfoList';
import { listTableInfoByPage } from '@/services/tableInfoService';
import { Link, useModel } from '@umijs/max';
import { Button, Card, Empty, Input, message, Space, Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import './index.less';
import { PlusOutlined, SearchOutlined, LoginOutlined } from '@ant-design/icons';

// 默认分页大小
const DEFAULT_PAGE_SIZE = 10;

interface Props {
  title?: string;
  needLogin?: boolean;
  showTag?: boolean;
  type?: 'public' | 'private';
  onLoad?: (
    searchParams: TableInfoType.TableInfoQueryRequest,
    setDataList: (dataList: TableInfoType.TableInfo[]) => void,
    setTotal: (total: number) => void,
  ) => void;
  onImport?: (values: TableInfoType.TableInfo) => void;
}

const TableInfoCard: React.FC<Props> = (props) => {
  const { title, needLogin = false, showTag = true, type = 'public', onLoad, onImport } = props;

  // 公开数据
  const [dataList, setDataList] = useState<TableInfoType.TableInfo[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);
  const initSearchParams: TableInfoType.TableInfoQueryRequest = {
    current: 1,
    pageSize: DEFAULT_PAGE_SIZE,
    sortField: 'createTime',
    sortOrder: 'descend',
  };
  const [searchParams, setSearchParams] =
    useState<TableInfoType.TableInfoQueryRequest>(initSearchParams);

  const { initialState } = useModel('@@initialState');
  const loginUser = initialState?.loginUser;

  /**
   * 加载数据
   */
  const innerOnLoad = () => {
    listTableInfoByPage({
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
    <div className={`table-info-card ${type}`}>
      <Card
        title={
          <div className="card-header">
            <span className="card-title">{title}</span>
            {type === 'public' ? (
              <Tag color="blue" className="type-tag">公开</Tag>
            ) : (
              <Tag color="green" className="type-tag">私有</Tag>
            )}
          </div>
        }
        extra={
          <Link to="/tableInfo/add">
            <Button type="primary" icon={<PlusOutlined />}>
              创建表
            </Button>
          </Link>
        }
        className="custom-card"
      >
        {!needLogin || loginUser ? (
          <>
            <Space direction="vertical" style={{ width: '100%' }} size="large">
              <Input.Search
                placeholder="请输入名称搜索"
                enterButton={<SearchOutlined />}
                size="large"
                className="search-input"
                onSearch={(value) => {
                  setSearchParams({
                    ...initSearchParams,
                    name: value,
                  });
                }}
              />
              <TableInfoList
                pagination={{
                  total,
                  onChange: (current) => {
                    setSearchParams({ ...searchParams, current });
                    window.scrollTo({ top: 0, behavior: 'smooth' });
                  },
                  pageSize: DEFAULT_PAGE_SIZE,
                }}
                showTag={showTag}
                dataList={dataList}
                loading={loading}
                onImport={onImport}
              />
            </Space>
          </>
        ) : (
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description={
              <div className="login-prompt">
                <div className="prompt-text">请先登录后查看</div>
                <Link to="/user/login">
                  <Button type="primary" size="large" icon={<LoginOutlined />}>
                    立即登录
                  </Button>
                </Link>
              </div>
            }
          />
        )}
      </Card>
    </div>
  );
};

export default TableInfoCard;
