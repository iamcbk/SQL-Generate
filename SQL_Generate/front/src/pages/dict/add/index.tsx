import { addDict, getDictById, listMyDict } from '@/services/dictService';
import {
  PageContainer,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { message } from 'antd';
import { useForm } from 'antd/es/form/Form';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'umi';
import { Card, Space, Typography, Divider } from 'antd';
import { BookOutlined, ImportOutlined, PlusOutlined } from '@ant-design/icons';

const { Paragraph } = Typography;

/**
 * 词条创建页面
 * @constructor
 */
const DictAddPage: React.FC<unknown> = () => {
  const [dictList, setDictList] = useState<DictType.Dict[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [form] = useForm();
  const navigate = useNavigate();

  // 获取可选词库列表
  useEffect(() => {
    setLoading(true);
    listMyDict({})
      .then((res) => {
        setDictList(res.data);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      })
      .finally(() => setLoading(false));
  }, []);

  /**
   * 创建
   * @param fields
   */
  const doAdd = async (fields: DictType.DictAddRequest) => {
    const hide = message.loading('正在提交');
    try {
      await addDict(fields);
      message.success('创建成功');
      navigate('/dict/add_result');
    } catch (e: any) {
      message.error('创建失败，请重试！', e.message);
    } finally {
      hide();
    }
  };

  /**
   * 导入基础词库
   * @param id
   */
  const loadDict = (id: number) => {
    if (!id) {
      form.setFieldValue('content', '');
      return;
    }
    getDictById(id)
      .then((res) => {
        form.setFieldValue('content', JSON.parse(res.data.content).join(','));
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  return (
    <PageContainer title={false}>
      <Card 
        bordered={false}
        style={{ 
          maxWidth: 800, 
          margin: '0 auto',
          borderRadius: '12px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.08)'
        }}
      >
        <Space direction="vertical" size="large" style={{ width: '100%' }}>
          <ProForm<DictType.DictAddRequest>
            form={form}
            onFinish={async (values) => {
              doAdd(values);
            }}
            labelAlign="left"
            layout="vertical"
            submitter={{
              submitButtonProps: {
                size: 'large',
                style: {
                  minWidth: 200,
                  height: 48,
                  borderRadius: '24px',
                  background: 'linear-gradient(to right, #1890ff, #36cfc9)',
                  border: 'none',
                  boxShadow: '0 2px 8px rgba(24,144,255,0.35)',
                },
                icon: <PlusOutlined />,
              },
              resetButtonProps: {
                size: 'large',
                style: {
                  minWidth: 120,
                  height: 48,
                  borderRadius: '24px',
                  border: '1px solid #d9d9d9',
                }
              },
              render: (props, dom) => (
                <div style={{ textAlign: 'center', marginTop: 48 }}>
                  {[...dom.reverse()]}
                </div>
              ),
            }}
          >
            <ProFormText
              name="name"
              label="词库名称"
              rules={[{ required: true }, { max: 30 }]}
              fieldProps={{
                size: 'large',
                prefix: <BookOutlined style={{ color: '#1890ff' }} />,
                style: {
                  height: 48,
                  borderRadius: '8px',
                }
              }}
              placeholder="给你的词库起个名字吧（最多30字）"
            />
            
            <ProFormSelect
              disabled={loading}
              options={dictList.map((dict) => ({
                value: dict.id,
                label: dict.name,
              }))}
              name="parent"
              label="基础词库"
              placeholder="选择一个基础词库作为模板"
              fieldProps={{
                size: 'large',
                onChange: loadDict,
                style: {
                  height: 48,
                  borderRadius: '8px',
                }
              }}
            />
            
            <ProFormTextArea
              name="content"
              label="单词列表"
              placeholder="输入单词，用英文逗号(,)或中文逗号(，)分隔"
              rules={[{ required: true }]}
              fieldProps={{
                maxLength: 20000,
                showCount: true,
                autoSize: { minRows: 8 },
                style: { 
                  fontSize: '14px',
                  borderRadius: '8px',
                  padding: '12px 16px',
                  lineHeight: '1.8',
                  backgroundColor: '#fafafa',
                },
              }}
            />
          </ProForm>
        </Space>
      </Card>
    </PageContainer>
  );
};

export default DictAddPage;
