import React from 'react';

const Footer: React.FC = () => (
  <footer className="footer">
    <div className="footer-content">
      <p>&copy; {new Date().getFullYear()} PookieTalk. All rights reserved.</p>
    </div>
  </footer>
);

export default Footer;
