import Logo from '@/assets/logo.png';
import { userRegister } from '@/services/userService';
import { Link } from '@@/exports';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import { message } from 'antd';
import { useNavigate } from 'umi';

/**
 * 用户注册页面
 */
export default () => {
  const navigate = useNavigate();

  /**
   * 用户注册
   * @param fields
   */
  const doUserRegister = async (fields: UserType.UserRegisterRequest) => {
    const hide = message.loading('注册中');
    try {
      await userRegister({ ...fields });
      hide();
      message.success('注册成功');
      navigate('/user/login', {
        replace: true,
      });
    } catch (e: any) {
      hide();
      message.error('注册失败，' + e.message);
    }
  };

  return (
    <div
      style={{
        height: '100vh',
        background: 'linear-gradient(135deg, #1a365d 0%, #2d3748 100%)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <div
        style={{
          display: 'flex',
          width: '1000px',
          height: '600px',
          background: 'white',
          borderRadius: '20px',
          overflow: 'hidden',
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
        }}
      >
        {/* 左侧装饰区域 */}
        <div
          style={{
            flex: '1',
            background: 'linear-gradient(135deg, #4299e1 0%, #2b6cb0 100%)',
            padding: '40px',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            color: 'white',
          }}
        >
          <img src={Logo} alt="Logo" style={{ width: '120px', marginBottom: '30px' }} />
          <h1 style={{ fontSize: '2.5em', margin: '0 0 20px 0' }}>SQL之父</h1>
          <p style={{ fontSize: '1.2em', textAlign: 'center', lineHeight: '1.6' }}>
            欢迎加入我们，开启智能编程之旅
          </p>
        </div>

        {/* 右侧注册表单区域 */}
        <div
          style={{
            flex: '1',
            padding: '40px',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            overflowY: 'auto',
          }}
        >
          <LoginForm<UserType.UserRegisterRequest>
            logo=""
            title="创建账号"
            subTitle="请填写以下信息完成注册"
            submitter={{
              searchConfig: {
                submitText: '注册',
              },
            }}
            onFinish={async (formData) => {
              await doUserRegister(formData);
            }}
            style={{
              maxWidth: '400px',
              margin: '0 auto',
            }}
          >
            <ProFormText
              name="userName"
              fieldProps={{
                size: 'large',
                prefix: <UserOutlined style={{ color: '#4299e1' }} />,
              }}
              placeholder={'请输入用户名'}
              rules={[
                {
                  required: true,
                  message: '请输入用户名!',
                },
              ]}
            />
            <ProFormText
              name="userAccount"
              fieldProps={{
                size: 'large',
                prefix: <UserOutlined style={{ color: '#4299e1' }} />,
              }}
              placeholder={'请输入账号（至少 4 位）'}
              rules={[
                {
                  required: true,
                  message: '请输入账号!',
                },
              ]}
            />
            <ProFormText.Password
              name="userPassword"
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined style={{ color: '#4299e1' }} />,
              }}
              placeholder={'请输入密码（至少 8 位）'}
              rules={[
                {
                  required: true,
                  message: '请输入密码！',
                },
              ]}
            />
            <ProFormText.Password
              name="checkPassword"
              fieldProps={{
                size: 'large',
                prefix: <LockOutlined style={{ color: '#4299e1' }} />,
              }}
              placeholder={'请再次输入密码'}
              rules={[
                {
                  required: true,
                  message: '请输入确认密码！',
                },
              ]}
            />

            <div
              style={{
                marginTop: 24,
                marginBottom: 24,
                display: 'flex',
                justifyContent: 'space-between',
              }}
            >
              <Link to="/user/login" style={{ color: '#4299e1' }}>
                已有账号？立即登录
              </Link>
              <Link to="/" style={{ color: '#4299e1' }}>
                返回主页
              </Link>
            </div>
          </LoginForm>
        </div>
      </div>
    </div>
  );
};
