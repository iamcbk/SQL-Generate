import TableInfoCreateModal from '@/components/TableInfoModal/TableInfoCreateModal';
import { history } from '@umijs/max';
import React from 'react';

const TableInfoAdd: React.FC = () => {
  return (
    <TableInfoCreateModal
      modalVisible={true}
      onSubmit={() => {
        history.push('/tableInfo');
      }}
      onCancel={() => {
        history.push('/tableInfo');
      }}
    />
  );
};

export default TableInfoAdd; 