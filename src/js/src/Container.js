import React from 'react';

export const Container = props => (
    <div style={{width: 'auto', margin:'0 auto', textAlign: 'center'}}>
        {props.children}
    </div>
);

export default Container;