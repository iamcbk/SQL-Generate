import { AUTO_INPUT_EXAMPLE } from '@/constants/examples';
import { getSchemaByAuto } from '@/services/sqlService';
import { Button, Form, message, Modal, Space } from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React from 'react';
import './index.less';
import { BulbOutlined } from '@ant-design/icons';

interface Props {
  onSubmit: (values: TableSchema) => void;
  visible: boolean;
  onClose: () => void;
}

const AutoInputModal: React.FC<Props> = (props) => {
  const { visible, onSubmit, onClose } = props;
  const [form] = Form.useForm();

  /**
   * 自动生成 schema
   * @param values
   */
  const onFinish = async (values: any) => {
    if (!values.content) {
      return;
    }
    try {
      const res = await getSchemaByAuto(values);
      onSubmit?.(res.data);
    } catch (e: any) {
      message.error('导入错误，' + e.message);
    }
  };

  return (
    <Modal 
      title={
        <div className="modal-title">
          <BulbOutlined className="title-icon" />
          <span>智能导入</span>
        </div>
      }
      open={visible} 
      footer={null} 
      onCancel={onClose}
      width={720}
      className="auto-input-modal"
    >
      <Form form={form} layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="content"
          label={
            <Space className="form-label">
              <span>请输入表的列名，多个列以【英文或中文逗号】分隔：</span>
              <Button
                type="link"
                size="small"
                onClick={() => form.setFieldValue('content', AUTO_INPUT_EXAMPLE)}
              >
                导入示例
              </Button>
            </Space>
          }
          rules={[{ required: true, message: '请输入配置' }]}
        >
          <TextArea
            placeholder="请输入表的列名，多个列以【英文或中文逗号】分隔："
            autoSize={{ minRows: 16 }}
            className="custom-textarea"
          />
        </Form.Item>
        <Form.Item>
          <Space size="large" className="form-buttons">
            <Button type="primary" htmlType="submit" size="large">
              导入
            </Button>
            <Button htmlType="reset" size="large">
              重置
            </Button>
          </Space>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AutoInputModal;
