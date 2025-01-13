import Logo from '@/assets/logo.png';
import { userLogin } from '@/services/userService';
import { Link } from '@@/exports';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { message } from 'antd';
import { useSearchParams } from 'umi';

/**
 * 用户登录页面
 */
export default () => {
  const [searchParams] = useSearchParams();

  const { initialState, setInitialState } = useModel('@@initialState');

  /**
   * 用户登录
   * @param fields
   */
  const doUserLogin = async (fields: UserType.UserLoginRequest) => {
    const hide = message.loading('登录中');
    try {
      const res = await userLogin({ ...fields });
      message.success('登录成功');
      setInitialState({
        ...initialState,
        loginUser: res.data,
      } as InitialState);
      // 重定向到之前页面
      window.location.href = searchParams.get('redirect') ?? '/';
    } catch (e: any) {
      message.error(e.message);
    } finally {
      hide();
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
          <h1 style={{ fontSize: '2.5em', margin: '0 0 20px 0' }}>SQL Generate Tool</h1>
          <p style={{ fontSize: '1.2em', textAlign: 'center', lineHeight: '1.6' }}>
            为您快速生成SQL语句及数据，提高您的开发效率
          </p>
        </div>

        {/* 右侧登录表单区域 */}
        <div
          style={{
            flex: '1',
            padding: '40px',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
          }}
        >
          <LoginForm<UserType.UserLoginRequest>
            logo=""
            title="欢迎回来"
            subTitle="请登录您的账号"
            onFinish={async (formData) => {
              await doUserLogin(formData);
            }}
            style={{
              maxWidth: '400px',
              margin: '0 auto',
            }}
          >
            <ProFormText
              name="userAccount"
              fieldProps={{
                size: 'large',
                prefix: <UserOutlined style={{ color: '#4299e1' }} />,
              }}
              placeholder={'请输入账号'}
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
              placeholder={'请输入密码'}
              rules={[
                {
                  required: true,
                  message: '请输入密码！',
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
              <Link to="/user/register" style={{ color: '#4299e1' }}>
                新用户注册
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
