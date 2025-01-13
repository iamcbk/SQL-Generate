import {
  BugOutlined,
  GithubOutlined,
  SketchOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';
import './index.less';

const GlobalFooter: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <DefaultFooter
      className="default-footer"
      copyright={`${currentYear} 严谨用作商业用途 违者追究法律责任`}
      links={[
        {
          key: 'master',
          title: (
            <>
              <UserOutlined /> 作者：蔡炳堃
            </>
          ),
          href: '',
          blankTarget: true,
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> 代码已开源
            </>
          ),
          href: 'https://github.com/iamcbk/SQL-Generate',
          blankTarget: true,
        }
      ]}
    />
  );
};

export default GlobalFooter;
