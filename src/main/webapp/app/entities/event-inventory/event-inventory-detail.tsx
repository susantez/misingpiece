import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './event-inventory.reducer';
import { IEventInventory } from 'app/shared/model/event-inventory.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEventInventoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class EventInventoryDetail extends React.Component<IEventInventoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { eventInventoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="misingpieceApp.eventInventory.detail.title">EventInventory</Translate> [<b>{eventInventoryEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemCount">
                <Translate contentKey="misingpieceApp.eventInventory.itemCount">Item Count</Translate>
              </span>
            </dt>
            <dd>{eventInventoryEntity.itemCount}</dd>
            <dt>
              <span id="creationDate">
                <Translate contentKey="misingpieceApp.eventInventory.creationDate">Creation Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={eventInventoryEntity.creationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="misingpieceApp.eventInventory.product">Product</Translate>
            </dt>
            <dd>{eventInventoryEntity.product ? eventInventoryEntity.product.id : ''}</dd>
            <dt>
              <Translate contentKey="misingpieceApp.eventInventory.event">Event</Translate>
            </dt>
            <dd>{eventInventoryEntity.event ? eventInventoryEntity.event.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/event-inventory" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/event-inventory/${eventInventoryEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ eventInventory }: IRootState) => ({
  eventInventoryEntity: eventInventory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EventInventoryDetail);
